<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
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
			$('#btnAll').click(function () {
				/*$("#problemstatus").val("已解决");*/
                $("#searchForm").submit();
			})
        $('#btnToBeSolved').click(function () {
            $("#problemstatus").val("UNRESOLVED");
            $("#searchForm").submit();
        })
		});

        function changeFeedback(obj){
            var id = $(obj).attr("feedbackId");
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

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
            $("#searchForm").attr("action","${ctx}/dunning/tMisCustomerServiceFeedback/notify");
			$("#searchForm").submit();
        	return false;
        }

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">通知列表</li>
	</ul>
	<form:form id="searchForm" modelAttribute="TMisCustomerServiceFeedback" action="${ctx}/dunning/tMisCustomerServiceFeedback/notify" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>类别：</label>
				<form:select path="hashtag" htmlEscape="false" maxlength="200" class="input-medium">
					<form:option selected="selected" value="" label="不限"/>
					<form:option  value="ORDER_DEDUCT" label="订单代扣"/>
					<form:option  value="WRITE_OFF" label="催销账"/>
					<form:option  value="COMPLAIN_SHAKE" label="投诉催收"/>
					<form:option  value="CONSULT_REPAY" label="协商还款"/>
					<form:option  value="CONTACT_REMARK" label="备注联系方式"/>
				</form:select>
			</li>
			<li><label>关键词：</label>
				<form:input path="keyword"  htmlEscape="false" maxlength="200" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
		<input id="problemstatus" name="problemstatus" type="hidden"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width: 800px">
		<thead>
		    <tr>
				<th style="background: #26d6ff" colspan="2">
					<span type="button" id="btnAll" value="" style="cursor: pointer;">全部</span>&nbsp;|&nbsp;
					<span type="button" id="btnToBeSolved" value="待解决" style="cursor: pointer;color: #1a1a1a">待解决</span>
				</th>
			</tr>
			<tr>
				<th style="width: 500px" >标题</th>
				<th>时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tMisCustomerServiceFeedback">
			<tr class="result">
				<td><a href="javascript: void 0;" feedbackId="${tMisCustomerServiceFeedback.id}" onclick="changeFeedback(this);">
						<span class="solveStatus">
							客服消息:订单号{${fns:abbr(tMisCustomerServiceFeedback.dealcode,50)}}
							<c:if test="${tMisCustomerServiceFeedback.statusText eq '已解决'}">
								${fns:abbr(tMisCustomerServiceFeedback.statusText,50)}
							</c:if>
							<c:if test="${tMisCustomerServiceFeedback.statusText eq '未解决'}">
								${fns:abbr(tMisCustomerServiceFeedback.tagText,50)}
							</c:if>
						</span>
				    </a>
				</td>
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