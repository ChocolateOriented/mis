<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		    if("${color}"=='all'){
                $('#btnAll').css("color","red");
            }else{

                $('#btnToBeSolved').css("color","red");
            }
		    function showRow(type){
                $('.result .solveStatus').each(function (index,item) {
                    $(item).parents('.result').show()
                    var status = $(item).html()
					if(type === 'all'){
                        if(status.indexOf('已解决') === -1){
                            $(item).parents('.result').hide()
						}
					}else if(type ==='unsolved'){
						if(status.indexOf('已解决')!==-1){
                            $(item).parents('.result').hide()

                        }
					}

                })
			}
//			$('#btnAll').click(function () {
//                $("#colorChoice").val("all");
//				$("#problemstatus").val("ALL");
//                $("#searchForm").submit();
//
//			})
//        $('#btnToBeSolved').click(function () {
//            $("#colorChoice").val("solving");
//            $("#problemstatus").val("UNRESOLVED");
//            $("#searchForm").submit();
//
//        })
		});

        function changeFeedback(flag,obj){
            var id = $(obj).attr("feedbackId");
            $(obj).children("span").css("color", "#999999");
            if(flag == 0){
                $.jBox.open("iframe:" + "${ctx}/dunning/tMisCustomerServiceFeedback/feedbackJbox2?id=" + id, "" , 480, 180,{
                    buttons: {
                    },
                    submit: function (v, h, f) {
                    },
                    loaded: function (h) {
                        $(".jbox-content", document).css("overflow-y", "hidden");
                    }
                });
            }else {
                $.jBox.open("iframe:" + "${ctx}/dunning/tMisCustomerServiceFeedback/feedbackJbox?id=" + id, "" , 480, 180,{
                    buttons: {
                    },
                    submit: function (v, h, f) {
                    },
                    loaded: function (h) {
                        $(".jbox-content", document).css("overflow-y", "hidden");
                    }
                });
            }


        }

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			if("${color}"=='all'){
                $("#colorChoice").val("all");
            }
            if("${color}"=='solving'){
                $("#colorChoice").val("solving");
            }
			$("#searchForm").submit();
        	return false;
        }

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">通知列表</li>
	</ul>
	<form:form id="searchForm" modelAttribute="tMisCustomerServiceFeedback" action="${ctx}/dunning/tMisCustomerServiceFeedback/notify" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="colorChoice" name="colorChoice" type="hidden" value=""/>
		<ul class="ul-form">
			<li>状态：
				<form:select path="problemstatus" htmlEscape="false" maxlength="100" class="input-small">
					<form:option selected="selected" value="" label="全部"/>
					<form:option  value="RESOLVED" label="已解决"/>
					<form:option  value="UNRESOLVED" label="未解决"/>

				</form:select>
			</li>
			<li><label>类别：</label>
				<form:select path="hashtag" htmlEscape="false" maxlength="200" class="input-small">
					<form:option selected="selected" value="" label="不限"/>
					<form:option  value="ORDER_DEDUCT" label="订单代扣"/>
					<form:option  value="WRITE_OFF" label="/78"/>
					<form:option  value="COMPLAIN_SHAKE" label="投诉催收"/>
					<form:option  value="CONSULT_REPAY" label="协商还款"/>
					<form:option  value="CONTACT_REMARK" label="备注联系方式"/>
				</form:select>
			</li>
			<li><label>关键词：</label>
				<form:input path="keyword"  htmlEscape="false" maxlength="200" class="input-small"/>
			</li>
			<li><label>推送时间</label>
				<input name="farPushTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${tMisCustomerServiceFeedback.farPushTime}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 至
				<input name="nearPushTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					   value="<fmt:formatDate value="${tMisCustomerServiceFeedback.nearPushTime}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"  onclick="return page()"/></li>
			<li class="clearfix"></li>
		</ul>
		<%--<form:input id="problemstatus" type="hidden" path="problemstatus"/>--%>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width: 1080px">
		<thead>
		    <%--<tr>--%>
				<%--<th style="background: #26d6ff" colspan="2">--%>
					<%--<span type="button" id="btnAll" value="" style="cursor: pointer;">全部</span>&nbsp;|&nbsp;--%>
					<%--<span type="button" id="btnToBeSolved" value="待解决" style="cursor: pointer;color: #1a1a1a;">待解决</span>--%>
				<%--</th>--%>
			<%--</tr>--%>
			<tr>
				<th style="width: 650px" >标题</th>
				<th>时间</th>
			</tr>
		</thead>
		<tbody>
		<c:set var="flag" value="0" ></c:set>
		<c:forEach items="${page.list}" var="tMisCustomerServiceFeedback">
			<tr class="result">
				<%--<td><a href="javascript: void 0;" feedbackId="${tMisCustomerServiceFeedback.id}" onclick="changeFeedback(${flag},this);">--%>
					<c:if test="${!(fns:getUser().name eq '系统管理员')}">
					<shiro:hasPermission name="dunning:tMisCustomerServiceFeedback:OnlyCommissionerview">
							<c:set var="flag" value="1" ></c:set>
					<td><a href="javascript: void 0;" feedbackId="${tMisCustomerServiceFeedback.id}" onclick="changeFeedback(${flag},this);">
							<%--${tMisCustomerServiceFeedback.readFlag }--%>
						<c:choose>
						<c:when test="${tMisCustomerServiceFeedback.readFlag eq '1'or fns:getUser() ne tMisCustomerServiceFeedback.dunningpeopleid}">
							<span  class="solveStatus" style="color: #999999" >
							客服消息:订单号{${fns:abbr(tMisCustomerServiceFeedback.dealcode,50)}}
							<c:if test="${tMisCustomerServiceFeedback.statusText eq '已解决'}">
								${fns:abbr(tMisCustomerServiceFeedback.tagText,50)}&nbsp;${fns:abbr(tMisCustomerServiceFeedback.statusText,50)}
							</c:if>
							<c:if test="${tMisCustomerServiceFeedback.statusText eq '未解决'}">
								${fns:abbr(tMisCustomerServiceFeedback.tagText,50)}
							</c:if>
						</span>
						</c:when>
					<%--</c:choose>--%>
						<c:otherwise>
						<span class="solveStatus">
							客服消息:订单号{${fns:abbr(tMisCustomerServiceFeedback.dealcode,50)}}
							<c:if test="${tMisCustomerServiceFeedback.statusText eq '已解决'}">
								${fns:abbr(tMisCustomerServiceFeedback.tagText,50)}&nbsp;${fns:abbr(tMisCustomerServiceFeedback.statusText,50)}
							</c:if>
							<c:if test="${tMisCustomerServiceFeedback.statusText eq '未解决'}">
								${fns:abbr(tMisCustomerServiceFeedback.tagText,50)}
							</c:if>
						</span>
						</c:otherwise>
					</c:choose>
					</shiro:hasPermission>
					</c:if>


					<c:if test="${flag == 0}">
					<td><a href="javascript: void 0;" feedbackId="${tMisCustomerServiceFeedback.id}" onclick="changeFeedback(${flag},this);">
						<span class="solveStatus">
							客服消息:订单号{${fns:abbr(tMisCustomerServiceFeedback.dealcode,50)}}
							<c:if test="${tMisCustomerServiceFeedback.statusText eq '已解决'}">
								${fns:abbr(tMisCustomerServiceFeedback.tagText,50)}&nbsp;${fns:abbr(tMisCustomerServiceFeedback.statusText,50)}
							</c:if>
							<c:if test="${tMisCustomerServiceFeedback.statusText eq '未解决'}">
								${fns:abbr(tMisCustomerServiceFeedback.tagText,50)}
							</c:if>

						</span>
					</c:if>


				<td>
					<fmt:formatDate value="${tMisCustomerServiceFeedback.pushTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>