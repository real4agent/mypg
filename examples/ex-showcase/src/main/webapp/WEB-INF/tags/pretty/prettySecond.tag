<%@ tag import="com.realaicy.pg.core.utils.PrettyTimeUtils" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="seconds" type="java.lang.Integer" required="true" description="秒" %>
<%=PrettyTimeUtils.prettySeconds(seconds == null ? 0 : seconds)%>