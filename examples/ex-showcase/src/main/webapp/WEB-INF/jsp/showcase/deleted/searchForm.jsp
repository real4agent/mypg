<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form id="searchForm" class="form-inline search-form" data-change-search="true">


    <pgform:label path="search.id_in">编号</pgform:label>
    <pgform:input path="search.id_in" cssClass="input-medium" placeholder="多个使用空格分隔"/>
    &nbsp;
    <pgform:label path="search.deleted_eq">已删除</pgform:label>
    <pgform:select path="search.deleted_eq" cssClass="input-small">
        <pgform:option label="全部" value=""/>
        <pgform:options items="${booleanList}" itemLabel="info"/>
    </pgform:select>

    &nbsp;
    <button type="submit" class="btn ">查询</button>
    <a class="btn btn-link accordion-toggle" data-toggle="collapse" href="#searchMore">高级查询</a>
    <a class="btn btn-link btn-clear-search">清空</a>

    <%--more--%>
    <div id="searchMore" class="accordion-body collapse">
        <div class="accordion-inner">

            <pgform:label path="search.birthday_gte">出生日期从</pgform:label>

            <div class="input-append date">
                <pgform:input path="search.birthday_gte" cssClass="input-medium"
                              data-format="yyyy-MM-dd hh:mm:ss"
                              placeholder="大于等于"/>
                <span class="add-on"><i data-time-icon="icon-time" data-date-icon="icon-calendar"></i></span>
            </div>
            <pgform:label path="search.birthday_lte">到</pgform:label>
            <div class="input-append date">
                <pgform:input path="search.birthday_lte" cssClass="input-medium"
                              data-format="yyyy-MM-dd hh:mm:ss"
                              data-position="bottom-left"
                              placeholder="小于等于"/>
                <span class="add-on"><i data-time-icon="icon-time" data-date-icon="icon-calendar"></i></span>
            </div>
        </div>
    </div>
</form>
