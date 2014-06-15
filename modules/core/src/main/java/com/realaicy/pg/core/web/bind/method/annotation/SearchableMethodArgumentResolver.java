package com.realaicy.pg.core.web.bind.method.annotation;

import com.google.common.collect.Lists;
import com.realaicy.pg.core.entity.search.Searchable;
import com.realaicy.pg.core.web.bind.annotation.SearchableDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;
import java.util.Map;

/**
 * 请求查询参数字符串及分页/排序参数绑定到Searchable
 * <pre>
 *     查询参数格式如下：
 *     1.1、默认查询字符串
 *         search.baseInfo.realname_like=zhang
 *         search.age_lt=12
 *         排序及分页请参考 {@link PageableMethodArgumentResolver}
 *     1.2、控制器处理方法写法
 *        public void test(Searchable searchable);
 *
 *     2.1、自定义查询字符串
 *         foo_search.baseInfo.realname_like=zhang
 *         foo_search.age_lt=12
 *         test_search.age_gt=12
 *         排序及分页请参考 {@link PageableMethodArgumentResolver}
 *     2.2、控制器处理方法写法
 *        public void test(@Qualifier("foo") Searchable searchable1, @Qualifier("test") Searchable searchable2);
 *
 *     3.1、禁用查询时分页及排序
 *          public void test(@Search(page = false, sort = false) Searchable searchable);
 * </pre>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class SearchableMethodArgumentResolver extends BaseMethodArgumentResolver {

    private static final PageableMethodArgumentResolver DEFAULT_PAGEABLE_RESOLVER = new PageableMethodArgumentResolver();
    /**
     * 分页参数解析器
     */
    private PageableMethodArgumentResolver pageableMethodArgumentResolver = DEFAULT_PAGEABLE_RESOLVER;
    private static final String DEFAULT_SEARCH_PREFIX = "search";
    private String prefix = DEFAULT_SEARCH_PREFIX;

    /**
     * 设置查询参数前缀
     *
     * @param prefix 查询参数前缀
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setPageableMethodArgumentResolver(PageableMethodArgumentResolver pageableMethodArgumentResolver) {
        this.pageableMethodArgumentResolver = pageableMethodArgumentResolver;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Searchable.class.isAssignableFrom(parameter.getParameterType());
    }

    /**
     * 方法
     * 1.获取查找的前缀，即判断参数应该是 search.age_lt=12 还是 foo_search.age_lt=12
     * 2.调用基类方法获取传入的参数中的自定义参数并进行解析，解析的结果是返回一个map
     * 3.处理查询参数：如果没有自定义的且指定需要默认的，则构造默认的查询参数
     * 如果有自定义的的且指定需要默认的，则先构造默认的，然后再构造自定义的，且自定义的可以覆盖先前的默认的
     * 4.处理分页：调用 {@link com.realaicy.pg.core.web.bind.method.annotation.PageableMethodArgumentResolver }
     * 处理传入的分页相关的信息
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String searchPrefix = getSearchPrefix(parameter);

        Map<String, String[]> searcheableMap = getPrefixParameterMap(searchPrefix, webRequest, true);

        //如果返回的map包含元素则说明：方法（参数或者是注解）中含有除了“默认查询参数”之外的自定义的查询参数
        boolean hasCustomSearchFilter = searcheableMap.size() > 0;

        SearchableDefaults searchDefaults = getSearchableDefaults(parameter);

        //如果方法（参数或者是注解）中包括SearchableDefaults信息，
        //且“SearchableDefaults信息”中指定了“需要将自定义的查询和默认的查询merge”
        boolean needMergeDefault = searchDefaults != null && searchDefaults.merge();

        Searchable searchable = null;
        //自定义覆盖默认，所以先处理默认的
        if (needMergeDefault || !hasCustomSearchFilter) {
            searchable = getDefaultFromAnnotation(searchDefaults);
        }
        if (hasCustomSearchFilter) {
            if (searchable == null) {
                searchable = Searchable.newSearchable();
            }
            for (String name : searcheableMap.keySet()) {

                String[] mapValues = filterSearchValues(searcheableMap.get(name));
                if (mapValues.length == 1) {
                    if (name.endsWith("in")) {
                        searchable.addSearchParam(name, StringUtils.split(mapValues[0], ",; "));
                    } else {
                        searchable.addSearchParam(name, mapValues[0]);
                    }
                } else {
                    searchable.addSearchParam(name, mapValues);
                }
            }
        }

        Pageable pageable = (Pageable) pageableMethodArgumentResolver.resolveArgument
                (parameter, mavContainer, webRequest, binderFactory);
        //默认分页及排序
        if (searchDefaults == null) {
            searchable.setPage(pageable);
        }
        //needPage=true 分页及排序
        if (searchDefaults != null && searchDefaults.needPage()) {
            searchable.setPage(pageable);
        }
        //needPage=false needSort=true  不要分页，但排序
        if (searchDefaults != null && !searchDefaults.needPage() && searchDefaults.needSort()) {
            searchable.addSort(pageable.getSort());
        }

        return searchable;
    }

    @SuppressWarnings("unchecked")
    /**
     * 去除空值
     */
    private String[] filterSearchValues(String[] values) {
        List<String> result = Lists.newArrayList(CollectionUtils.arrayToList(values));
        for (int i = 0; i < result.size(); i++) {
            if (StringUtils.isBlank(result.get(i))) {
                result.remove(i);
            }
        }
        return result.toArray(values);
    }

    /**
     * 方法首先判断传入的方法参数是否有Qualifier关键字，如果有的话则将查找的前缀设置为qualifier.value() + "_" + 默认的prefix
     * 如果没有的话则直接用默认的
     */
    private String getSearchPrefix(MethodParameter parameter) {
        Qualifier qualifier = parameter.getParameterAnnotation(Qualifier.class);

        if (qualifier != null) {
            //return new StringBuilder(qualifier.value()).append("_").append(prefix).toString();
            return qualifier.value() + "_" + prefix;
        }

        return prefix;
    }

    /**
     * 查找默认的查询参数
     * 首先从传入方法的参数中查找，如果找不到然后从方法的签名中查找SearchableDefaults注解
     */
    private SearchableDefaults getSearchableDefaults(MethodParameter parameter) {
        //首先从参数上找
        SearchableDefaults searchDefaults = parameter.getParameterAnnotation(SearchableDefaults.class);
        //找不到从方法上找
        if (searchDefaults == null) {
            searchDefaults = parameter.getMethodAnnotation(SearchableDefaults.class);
        }
        return searchDefaults;
    }

    private Searchable getDefaultFromAnnotation(SearchableDefaults searchableDefaults) {

        Searchable searchable = defaultSearchable(searchableDefaults);
        if (searchable != null) {
            return searchable;
        }

        return Searchable.newSearchable();
    }

    private Searchable defaultSearchable(SearchableDefaults searchableDefaults) {

        if (searchableDefaults == null) {
            return null;
        }

        Searchable searchable = Searchable.newSearchable();
        for (String searchParam : searchableDefaults.value()) {
            String[] searchPair = searchParam.split("=");
            String paramName = searchPair[0];
            String paramValue = searchPair[1];
            if (paramName.endsWith("in")) {
                searchable.addSearchParam(paramName, StringUtils.split(paramValue, ",; "));
            } else {
                searchable.addSearchParam(paramName, paramValue);
            }
        }

        return searchable;
    }

}
