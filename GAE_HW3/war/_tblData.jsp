<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>

<tr id="${w.key}">
	<td class="img_orig"><img src="${w.img_url}" /></td>
	<td class="id">${w.key}</td>
	<td class="location">${w.city}, ${w.region}. <br>
		${w.country}
	</td>

	<td class="curr">${w.curr_text}</td>
	<td class="temp">${w.temp} C</td>
	<td class="low">${w.low} C</td>
	<td class="high">${w.high} C</td>

	<td class="forecast">${w.forecast_text}</td>
	<td class="humidity">${w.humidity}</td>
	<td class="visibility">${w.visibility}</td>

	<td class="sunrise">${w.sunrise}</td>
	<td class="sunset">${w.sunset}</td>

	<td class="updated">${w.timestamp}</td>

	<td class="img_new"><img src="./images?id=${w.key}" /></td>
</tr>