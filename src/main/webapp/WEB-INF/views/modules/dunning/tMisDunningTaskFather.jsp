<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收任务管理父页面</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.tag {
			color: #ffffff;
			border: solid 1px #fac450;
			border-radius: 3px;
			display: inline-block;
			font-size: 14px;
			font-weight: normal;
			padding: 1px;
			margin-right: 5px;
			position: relative;
		}

		#SensitiveOcp {
			background-color: #fac450;
		}

		#Complaint {
			background-color: #ff6600;
		}
		#Other {
			background-color: #008000;
		}

		.colortrue {
			border: solid #3daae9 1px;
			border-radius: 5px;
			margin-bottom: 5px;
			background-color: #f0f8fd;
			/*background-color: #e2ffd6;*/
			padding: 3px 10px 3px 10px;
		}

		.colorfalse {
			border: solid #3daae9 1px;
			border-radius: 5px;
			margin-bottom: 5px;
			background-color: #f0f8fd;
			padding: 3px 10px 3px 10px;
			/*background-color: #dbf8ff;*/
		}


		.suspense {
			z-index: 10000;
			position: absolute;
			top: 22px;
			left: -1px;
			/*background-color: #f0f8fd;*/
			opacity: 0.9;
			/*border: solid #3daae9 1px;*/
			/*border-radius: 5px;*/
			outline: none;
			color: #555;
			font-size: 13px;
			padding: 10px;
		}
	</style>
	<script type="text/javascript">
        $(document).ready(function() {
            if(${memberInfo eq null}){
                $("#customerTable5,#memberTitle").hide();
            }
            if("${ispayoff}" == "true"){
                disableBtn();
            }
            var obj=null;
            if(parseInt("${overdueDays}")>parseInt("${controlDay}")){
                obj=document.getElementById("customerDetails");
            }else{
                obj=document.getElementById("conclusion");
            }
            childPage(obj);
        });

        function collectionfunction(obj, width, height, param){
            var method = $(obj).attr("method");
            var contactMobile = $(obj).attr("contactMobile");
            var contactstype = $(obj).attr("contactstype");
            var contactsname = "${personalInfo.realName}";
            var url = "${ctx}/dunning/tMisDunningTask/collection" + method + "?buyerId=${buyerId}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&contactMobile=" + contactMobile + "&contactstype=" + contactstype+"&mobileSelf="+contactMobile;
            if (param) {
                for (var name in param) {

                    if (typeof param[name] != "function") {
                        url = url + "&" + name + "=" + param[name] || "";
                    }
                }
            }

            $.jBox.open("iframe:" + url, $(obj).attr("value") , width || 600, height || 430, {
                buttons: {//"确定": "ok", "取消": true
                },
                submit: function (v, h, f) {
                },
                loaded: function (h) {
                    $(".jbox-content", document).css("overflow-y", "hidden");
                    var iframeName = h.children(0).attr("name");
                    var iframeHtml = window.frames[iframeName];               //获取子窗口的句柄
                    iframeHtml.$("#contactsname").val(contactsname);
                }
            });
        }

        function changeCard(obj, width, height){
            $.get("${ctx}/dunning/tMisChangeCardRecord/preCheck", {dealcode:"${dealcode}"}, function(data) {
                if(data && data.result != "OK") {
                    $.jBox.tip(data.msg, "warning");
                    return;
                }

                var btn = $(obj);
                var url = "${ctx}/dunning/tMisChangeCardRecord/changeCard?buyerId=${buyerId}&dealcode=${dealcode}&mobile=${changeCardRecord.mobile}&idcard=${changeCardRecord.idcard}&bankname=${changeCardRecord.bankname}&bankcard=${changeCardRecord.bankcard}&changeType=" + btn.prop("id");
                $.jBox.open("iframe:" + url, btn.val() , width || 600, height || 430, {
                    buttons: {//"确定": "ok", "取消": true
                    },
                    submit: function (v, h, f) {
                    },
                    loaded: function (h) {
                        $(".jbox-content", document).css("overflow-y", "hidden");
                    }
                });
                btnStatistics(document, obj);
            });

        }

        //按钮点击统计
        function btnStatistics(doc, btn) {
            $.post("${ctx}/dunning/tMisBtnStatistics/save",
                {buyerid: "${buyerId}", buyername: "${personalInfo.realName}", dealcode: "${dealcode}", pagename: doc.title, btnid: $(btn).prop('id'), btnname: $(btn).val()},
                function(data) {});
        }

        //打开代扣页面前验证
        function deductPreCheck(callback, doc, btn) {
            var bankName;
            var bankCard;
            if ("${changeCardRecord}") {
                bankName = "${changeCardRecord.bankname}";
                bankCard = "${changeCardRecord.bankcard}";
            } else {
                bankName = "${personalInfo.remitBankName}";
                bankCard = "${personalInfo.remitBankNo}";
            }
            $.get("${ctx}/dunning/tMisDunningDeduct/preCheck", {dealcode:"${dealcode}", bankCard: bankCard, bankName: bankName}, function(data) {
                if(data && data.result != "OK") {
                    $.jBox.tip(data.msg, "warning");
                    return;
                }
                callback();
            });
            btnStatistics(doc, btn);
        }

        function disableBtn() {
            $("#btnTelTaskFather").prop("disabled", true);
            $("#changeIdcard").prop("disabled", true);
            $("#changeMobile").prop("disabled", true);
            $("#changeBankcard").prop("disabled", true);
            $("#butnSms").prop("disabled", true);
            $("#butnIdCardImg").prop("disabled", true);
        }

        function getBuyerIdCardImg() {
            $.jBox.open("<img src='${ctx}/dunning/tMisDunningTask/showBuyerIdCardImg?buyerId=${buyerId}'/>", "手持身份证", 800, 700, {
                buttons: {}
            });
        }

        //打开添加标签弹窗
        function tagPopup(obj) {
            $.get("${ctx}/dunning/tMisDunningTag/preCheck", {buyerid:"${buyerId}"}, function(data) {
                if(data == "OK") {
                    collectionfunction(obj, 540, 340);
                } else {
                    $.jBox.tip("无法添加更多标签", "warning");
                }
            });
        }

        function showTagDetail(obj) {
            $(".suspense").css("display", "none");
            $(obj).children(".suspense").css("display", "block");
        }

        function hideTagDetail() {
            $(".suspense").css("display", "none");
        }

        function editTag(obj) {
//            var tagId = $(obj).parent().attr("tagId");
//            collectionfunction($(obj).parent(), 540, 340, {tagId : tagId});
            var tagName = $(obj).parent().attr("tagName");
            collectionfunction($(obj).parent(), 540, 340, {tagName : tagName});
        }

        function closeRemark(obj) {
            var tagId = $(obj).parent().attr("tagId");
            confirmx('确认要删除该备注吗？', function() {
                $.post("${ctx}/dunning/tMisDunningTag/closeRemark", {id : tagId}, function(data) {
                    if (data == "OK") {
                        $(obj).parent().fadeOut(300);
                        var v = $(obj).parent().parent().children().length;
                        if(v==1){
                            $(obj).parent().parent().parent().remove();
						}
                        setTimeout(function() {
                            $(obj).parent().remove();
                        }, 300);
                    }
                });
            });
        }
        function closeTag (obj) {

            var tagName = $(obj).parent().attr("tagName");
            var buyerid=${buyerId};
            confirmx('确认要删除该标签吗？', function() {
                $.post("${ctx}/dunning/tMisDunningTag/closeTag", {tagName : tagName,buyerid : buyerid}, function(data) {
                    if (data == "OK") {
                        $(obj).parent().fadeOut(300);
                        setTimeout(function() {
                            $(obj).parent().remove();
                        }, 300);
                    }
                });
            });
        }

        //添加标签后添加元素
        function addTag(tagId) {
            $.get("${ctx}/dunning/tMisDunningTag/get", {id : tagId}, function(data) {
                var templ = $("#tagTemplate");
                var elem = templ.clone(true);
                elem.children("#title").text(data.tagtypeDesc);
                elem.children(".suspense").children().attr("class","colorfalse");
                elem.children(".suspense").attr("name",data.tagtype);
                elem.prop("id", data.tagtype);
                elem.attr("tagName", data.tagtype);
                elem.find("#remarkId").attr("tagId", data.id);
                elem.find("#tagtype span").text(data.tagtypeDesc);
                if (!data.occupation) {
                    elem.find("#occupation").remove();
                } else {
                    elem.find("#occupation span").text(data.occupationDesc);
                }
                elem.find("#remark span").text(data.remark);
                elem.find("#peoplename span").text(data.peoplename);
                elem.find("#updateDate span").text(data.updateDate);
                $("#tags").append(elem);
                $("#" + data.tagtype).fadeIn();
            });
        }

        //编辑后刷新标签明细
        function refreshTag(tagId) {
            $.get("${ctx}/dunning/tMisDunningTag/get", {id : tagId}, function(data) {
                var templ = $("#remarkId");
                var elem = templ.clone(true);
                elem.attr("tagId",data.id);
                if (!data.occupation) {
                    elem.find("#occupation").remove();
                } else {
                    elem.find("#occupation span").text(data.occupationDesc);
                }
                elem.find("#remark span").text(data.remark);
                elem.find("#peoplename span").text(data.peoplename);
                elem.find("#updateDate span").text(data.updateDate);
                elem.parent().parent().attr("name",data.tagtype);
                var a = $("div[name='"+data.tagtype+"']").children("div").attr("class");
                if(a == "colorfalse"){
                    elem.attr("class","colortrue");
				}else {
                    elem.attr("class","colorfalse");
				}
               $("div[name='"+data.tagtype+"']").prepend(elem);

//                var elem = $(".tag[tagId='" + tagId + "']");
//                elem.find("#tagtype span").text(data.tagtypeDesc);
//                if (data.occupation) {
//                    elem.find("#occupation span").text(data.occupationDesc);
//                }
//                elem.find("#remark span").text(data.remark);
//                elem.find("#peoplename span").text(data.peoplename);
//                elem.find("#updateDate span").text(data.updateDate);
//                $.jBox.tip("保存备注成功", "info");
            });
        }

        var param = "?mobileSelf=${mobileSelf}&buyerId=${buyerId}&thisCreditAmount=${personalInfo.creditAmount/100}&dealcode=${dealcode}&dunningtaskdbid=${dunningtaskdbid}&dunningCycle=${dunningCycle}&overdueDays=${overdueDays}";
        function childPage(obj){
            $(".nav li").removeClass("active");
            $(obj).parent().addClass("active");
            var url = $(obj).attr("url");
            var currentPageUrl = url + param;
            $("#ifm").attr("src", currentPageUrl);
        }

        //从父页面跳转到软电话页面
        function phoneFatherPage(phone,name){
            if(confirm("确定拨打该电话吗")){
                var phone = phone.replace(/(\s|_)/g, "");
                phone = phone.replace("+86", "");
                $.post("${ctx}/dunning/tMisDunningPhone/fatherPageTOPhonePage", {target:phone, peopleId: "${userId}",name:name}, function(data) {
                    if (!data) {
                        alert("当前坐席状态无法外呼");
                        return;
                    }
                });
            }
        }

        function messageHide(){
            return  parseInt("${overdueDays}")>parseInt("${controlDay}");

        }
	</script>
</head>
<body>
<h4>&nbsp;&nbsp; </h4>
<h4 style="display:inline-block;position:relative;top:5px;">&nbsp;&nbsp;个人信息&nbsp;&nbsp;</h4>
<%--<div id="tags" style="display:inline-block;margin-bottom:0px;padding:0px;font-size:0px;">--%>
	<%--<c:forEach items="${tags}" var="tag">--%>
		<%--<div id="${tag.tagtype}" class="tag" onmouseover="showTagDetail(this);" onmouseout="hideTagDetail();" tagId="${tag.id}" method="Tag">--%>
			<%--<span id="title" style="margin:0px 3px 0px 3px;">${tag.tagtype.desc}</span>--%>
			<%--<shiro:hasPermission name="dunning:tMisDunningTag:edit">--%>
				<%--<i id="editTag" class="icon-edit" style="cursor:pointer;" onclick="editTag(this);"></i>--%>
				<%--<span id="closeTag" onclick="closeTag(this);" style="cursor:pointer;margin:0px 3px 0px 0px;">&times;</span>--%>
			<%--</shiro:hasPermission>--%>
			<%--<div class="suspense" style="display:none;" tabindex="0">--%>
				<%--<div>--%>

				<%--<span id="closeTag" onclick="closeTag(this);" style="cursor:pointer;margin:0px 3px 0px 0px;">&times;</span>--%>
				<%--&lt;%&ndash;<div id="tagtype" style="white-space:nowrap;">敏感类型: <span>${tag.tagtype.desc}</span></div>&ndash;%&gt;--%>
				<%--<c:if test="${not empty tag.occupation.desc}">--%>
					<%--<div id="occupation" style="white-space:nowrap;">职业类型: <span>${tag.occupation.desc}</span></div>--%>
				<%--</c:if>--%>
				<%--<div id="remark" style="white-space:nowrap;">备注: <span>${tag.remark}</span></div>--%>
				<%--<div id="peoplename" style="white-space:nowrap;">标记人: <span>${tag.peoplename}</span></div>--%>
				<%--<div id="updateDate" style="white-space:nowrap;">标记时间: <span><fmt:formatDate value="${tag.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></div>--%>
				<%--</div>--%>

				<%--<div>aaa</div>--%>
			<%--</div>--%>
			<%--<div class="suspense" style="display:none;" tabindex="0">--%>
				<%--aaa--%>
			<%--</div>--%>
		<%--</div>--%>
	<%--</c:forEach>--%>
	<%--<div id="tagTemplate" class="tag" onmouseover="showTagDetail(this);" onmouseout="hideTagDetail();" tagId="" method="Tag" style="display:none;">--%>
		<%--<span id="title" style="margin:0px 3px 0px 3px;"></span>--%>
		<%--<shiro:hasPermission name="dunning:tMisDunningTag:edit">--%>
			<%--<i id="editTag" class="icon-edit" style="cursor:pointer;" onclick="editTag(this);"></i>--%>
			<%--<span id="closeTag" onclick="closeTag(this);" style="cursor:pointer;margin:0px 3px 0px 0px;">&times;</span>--%>
		<%--</shiro:hasPermission>--%>
		<%--<div class="suspense" style="display:none;" tabindex="0">--%>
			<%--<div id="tagtype" style="white-space:nowrap;">敏感类型: <span></span></div>--%>
			<%--<div id="occupation" style="white-space:nowrap;">职业类型: <span></span></div>--%>
			<%--<div id="remark" style="white-space:nowrap;">备注: <span></span></div>--%>
			<%--<div id="peoplename" style="white-space:nowrap;">标记人: <span></span></div>--%>
			<%--<div id="updateDate" style="white-space:nowrap;">标记时间: <span></span></div>--%>
		<%--</div>--%>
	<%--</div>--%>
<%--</div>--%>
<div id="tags" style="display:inline-block;margin-bottom:0px;padding:0px;font-size:0px;">
	<c:forEach items="${mapTag}" var="mapTag">
		<div id="${mapTag.key}" class="tag" onmouseover="showTagDetail(this);" onmouseout="hideTagDetail();"
			 tagName=${mapTag.key.name()} method="Tag">
			<span id="title" style="margin:0px 3px 0px 3px;">${mapTag.key.desc}</span>
			<shiro:hasPermission name="dunning:tMisDunningTag:edit">
				<i id="editTag" class="icon-edit" style="cursor:pointer;" onclick="editTag(this);"></i>
				<span id="closeTag" onclick="closeTag(this);"
					  style="cursor:pointer;margin:0px 3px 0px 0px;">&times;</span>
			</shiro:hasPermission>
			<div class="suspense" style="display:none;" tabindex="0" name="${mapTag.key}">
				<c:set var="colorId" value="false"/>
				<c:forEach items="${mapTag.value}" var="tag">
					<c:set var="colorId" value="${!colorId}"/>
				<div class="color${colorId}" tagId="${tag.id}">
					<shiro:hasPermission name="dunning:tMisDunningTag:edit">
					<span id="closeRemark" onclick="closeRemark(this);"
						  style="cursor:pointer;margin:0px 3px 0px 0px;float: right" >&times;</span>
					</shiro:hasPermission>
						<%--<div id="tagtype" style="white-space:nowrap;">敏感类型: <span>${tag.tagtype.desc}</span></div>--%>
					<c:if test="${not empty tag.occupation.desc}">
						<div id="occupation" style="white-space:nowrap;">职业类型: <span>${tag.occupation.desc}</span></div>
					</c:if>
					<div id="remark" ><div style='display:inline-block;vertical-align: top;'>备注: </div><span style='width: 130px;display:inline-block;word-break: break-all;'>${tag.remark}</span></div>
					<div id="peoplename" style="white-space:nowrap;">标记人: <span>${tag.peoplename}</span></div>
					<div id="updateDate" style="white-space:nowrap;">标记时间: <span><fmt:formatDate
							value="${tag.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></div>
				</div>
					<%--<div>&nbsp;</div>--%>
				</c:forEach>
			</div>

		</div>
	</c:forEach>
	<div id="tagTemplate" class="tag" onmouseover="showTagDetail(this);" onmouseout="hideTagDetail();" tagName=""
		 method="Tag" style="display:none;">
		<span id="title" style="margin:0px 3px 0px 3px;"></span>
		<shiro:hasPermission name="dunning:tMisDunningTag:edit">
			<i id="editTag" class="icon-edit" style="cursor:pointer;" onclick="editTag(this);"></i>
			<span id="closeTag" onclick="closeTag(this);" style="cursor:pointer;margin:0px 3px 0px 0px;">&times;</span>
		</shiro:hasPermission>
		<div class="suspense" style="display:none;" tabindex="0" >
			<div class="" id="remarkId" tagId="">
				<shiro:hasPermission name="dunning:tMisDunningTag:edit">
				<span id="closeRemark" onclick="closeRemark(this);"
					  style="cursor:pointer;margin:0px 3px 0px 0px;float: right" >&times;</span>
				</shiro:hasPermission>
				<%--<div id="tagtype" style="white-space:nowrap;">敏感类型: <span></span></div>--%>
				<div id="occupation" style="white-space:nowrap;">职业类型: <span></span></div>
				<div id="remark" ><div style='display:inline-block;vertical-align: top;'>备注: </div><span style='width: 130px;display:inline-block;word-break: break-all;'></span></div>
				<div id="peoplename" style="white-space:nowrap;">标记人: <span></span></div>
				<div id="updateDate" style="white-space:nowrap;">标记时间: <span></span></div>
			</div>
		</div>
	</div>
</div>
<input id="daikouStatus" name="daikouStatus" type="hidden" value="${daikouStatus}" />
<table id="customerTable" class="table table-striped table-bordered table-condensed">
	<input id="mobile" name="mobile" type="hidden" value="${personalInfo.mobile}" />
	<tbody>
	<tr>
		<td>手机号：<shiro:hasPermission name="dunning:phone:view">
			<a href="javascript:void(0)" onclick="phoneFatherPage('${personalInfo.mobile}','${personalInfo.realName}')" showName="${personalInfo.realName}">
				</shiro:hasPermission>
				${personalInfo.mobile}
				<shiro:hasPermission name="dunning:phone:view">
			</a>
			</shiro:hasPermission>
			(${personalInfo.mobileCity})&nbsp;&nbsp;
			<input id="btnTelTaskFather" class="btn btn-primary" type="button" value="电话" style="padding:0px 8px 0px 8px;font-size:13px;"
				   contactMobile="${personalInfo.mobile}" contactstype="SELF" onclick="collectionfunction(this, 650)" method="Tel"/>

			<input id="butnSms" style="padding:0px 8px 0px 8px;font-size:13px;"
				   name="btnCollection" onclick="collectionfunction(this)" class="btn btn-primary"  contactMobile="${personalInfo.mobile}" contactstype="self"  method="Sms" type="button" value="短信" />

			<shiro:hasPermission name="dunning:tMisDunningImage:view">
				<input id="butnIdCardImg" style="padding:0px 8px 0px 8px;font-size:13px;" onclick="getBuyerIdCardImg()" class="btn btn-primary" type="button" value="手持身份证" />
			</shiro:hasPermission>
		</td>
		<td>姓名：${personalInfo.realName}(${personalInfo.sex})(${not empty personalInfo.marital ? personalInfo.marital eq 'Y' ? "已婚" : "未婚" : "未获取"})</td>
		<td>身份证：${personalInfo.idcard}</td>
		<td>发薪日：${personalInfo.paydayDesc}</td>
	</tr>
	<tr>
		<td>证件地址：${not empty personalInfo.ocrAddr ? personalInfo.ocrAddr : "未获取"}</td>
		<td>常住地址：${not empty personalInfo.livingAddr ? personalInfo.livingAddr : "未获取"}</td>
		<td>C卡分数：${score}</td>
		<td>发薪区间（元）：${personalInfo.salaryDesc}</td>
	</tr>
	</tbody>
</table>
<h4>&nbsp;&nbsp;借款信息&nbsp;&nbsp;<span style="color:red; font-size:15px;">${fn:contains(personalInfo.finProduct, 'jinRongZhongXin') ?
		'weixin36' : 'mo9'}</span></h4>
<table id="customerTable2" class="table table-striped table-bordered table-condensed">
	<tbody>
	<tr>
		<td>本金：${personalInfo.corpusAmount/100}元</td>
		<td>服务费：${personalInfo.costAmout/100}元&nbsp;(${personalInfo.days}天)</td>
		<td>优惠金额：${personalInfo.discountAmount/100}元</td>
		<td>订单金额：${personalInfo.amount/100}元</td>
		<td>到期还款日：<fmt:formatDate value="${personalInfo.repaymentTime}" pattern="yyyy-MM-dd"/></td>
	</tr>

	<tr>
		<td>续期次数：${personalInfo.delayCount}次</td>
		<td>逾期费：${personalInfo.overdueAmount/100}元&nbsp;(${personalInfo.overdueDays}天)</td>
		<td>减免金额：${personalInfo.modifyAmount/100}元</td>
		<td colspan="2" style="color:red;">当前应催金额：${personalInfo.creditAmount/100}元</td>
	</tr>
	<tr>
		<td>还款总额：${personalInfo.balance/100}元</td>
		<td colspan="4">还清日期：<fmt:formatDate value="${personalInfo.payOffTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>
	</tbody>
</table>

<h4 id="memberTitle">&nbsp;&nbsp;会员信息&nbsp;&nbsp;<span style="color:red; font-size:15px;"></h4>
<table id="customerTable5" class="table table-striped table-bordered table-condensed">
	<tbody>
	<tr>
		<td>会员类型：${memberInfo.memberTypeText}</td>
		<td>会费标准：${memberInfo.memberStandard}</td>
		<td>会员费：${memberInfo.rechargeAmonut}元</td>
		<td>会员卡状态：${memberInfo.useTypeText}</td>
		<td>会员有效期：${memberInfo.validDate}天</td>
	</tr>

	<tr>
		<td>办理日期：<fmt:formatDate value="${memberInfo.startTime}" pattern="yyyy-MM-dd"/></td>
		<td>到期日期：<fmt:formatDate value="${memberInfo.overdueTime}" pattern="yyyy-MM-dd"/></td>
		<td>备注：${memberInfo.remark}</td>
		<td></td>
		<td></td>

	</tr>

	</tbody>
</table>

<h4>&nbsp;&nbsp;收款信息 </h4>
<table id="customerTable3" class="table table-striped table-bordered table-condensed">
	<tbody>
	<tr>
		<td>收款时间：<fmt:formatDate value="${personalInfo.remitTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		<td>收款银行：${not empty personalInfo.remitBankName ? personalInfo.remitBankName : "未知"}</td>
		<td>收款卡号：
			<c:choose>
				<c:when test="${not empty personalInfo.remitBankNo}">
					<c:out value="${fn:substring(personalInfo.remitBankNo, 0, 4)} **** **** ${fn:substring(personalInfo.remitBankNo, 12, 16)} ${fn:substring(personalInfo.remitBankNo, 16, -1)}" />
				</c:when>
				<c:otherwise>
					<c:out value="未知" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	</tbody>
</table>

<shiro:hasPermission name="dunning:tMisDunningDeduct:view">
	<h4>&nbsp;&nbsp;扣款信息&nbsp;&nbsp;<span style="color:red; font-size:15px;">${deductable ? '可' : '不可'}代扣</span></h4>
	<table id="customerTable4" class="table table-striped table-bordered table-condensed">
		<tbody>
		<tr>
			<td>身份证号：${not empty changeCardRecord.idcard ? changeCardRecord.idcard : "未知"}</td>
			<td>预留手机号：${not empty changeCardRecord.mobile ? changeCardRecord.mobile : "未知"}</td>
			<td>扣款银行：${not empty changeCardRecord.bankname ? changeCardRecord.bankname : "未知"}</td>
			<td>扣款卡号：
				<c:choose>
					<c:when test="${not empty changeCardRecord.bankcard}">
						<c:out value="${fn:substring(changeCardRecord.bankcard, 0, 4)} **** **** ${fn:substring(changeCardRecord.bankcard, 12, 16) } ${fn:substring(changeCardRecord.bankcard, 16, -1)}" />
					</c:when>
					<c:otherwise>
						<c:out value="未知" />
					</c:otherwise>
				</c:choose>
			</td>
			<td>添加人：${not empty changeCardRecord.createBy ? changeCardRecord.createBy.name : 'sys'}</td>
			<td>
				<input id="changeIdcard" class="btn btn-primary" type="button" value="换身份证" style="padding:0px 8px 0px 8px;" onclick="changeCard(this, null, 300)"/>&nbsp;
				<input id="changeMobile" class="btn btn-primary" type="button" value="换手机号" style="padding:0px 8px 0px 8px;" onclick="changeCard(this, null, 300)"/>&nbsp;
				<input id="changeBankcard" class="btn btn-primary" type="button" value="换卡" style="padding:0px 8px 0px 8px;" onclick="changeCard(this, null, 350)"/>
			</td>
		</tr>
		</tbody>
	</table>
</shiro:hasPermission>
<br/>
<ul class="nav nav-tabs">
	<c:if test="${overdueDays>controlDay}">
		<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a id="customerDetails" href="javascript:void 0;" url="${ctx}/dunning/tMisDunningTask/customerDetails" onclick="childPage(this)">单位&联系人</a></li></shiro:hasPermission>
		<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="javascript:void 0;" url="${ctx}/dunning/tMisDunningTask/communicationDetails" onclick="childPage(this)">关联人</a></li></shiro:hasPermission>
		<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="javascript:void 0;" url="${ctx}/dunning/tMisDunningTask/communicationRecord" onclick="childPage(this)">关联人联系记录</a></li></shiro:hasPermission>
	</c:if>
	<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="javascript:void 0;" url="${ctx}/dunning/tMisDunnedConclusion/list" id="conclusion" onclick="childPage(this)">电催结论记录</a></li></shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="javascript:void 0;" url="${ctx}/dunning/tMisContantRecord/list" onclick="childPage(this)">催款历史</a></li></shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:view"><li><a href="javascript:void 0;" url="${ctx}/dunning/tMisDunningTask/orderHistoryList" onclick="childPage(this)">历史借款信息</a></li></shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisRemittanceConfirm:insertForm">
		<c:if test="${not ispayoff}"><li><a href="javascript:void 0;" url="${ctx}/dunning/tMisRemittanceConfirm/insertRemittanceConfirmForm" onclick="childPage(this)">汇款信息</a></li></c:if>
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:view">
		<li><a href="javascript:void 0;" url="${ctx}/dunning/tMisDunningTask/apploginlogList" onclick="childPage(this)">登录日志</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningDeduct:view">
		<li><a href="javascript:void 0;" url="${ctx}/dunning/tMisDunningDeduct/list" onclick="childPage(this)">扣款信息</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisDunningTask:informationRecovery">
		<li><a id="informationRecovery" href="javascript:void 0;" url="${ctx}/dunning/tMisDunningTask/informationRecovery" onclick="childPage(this)">信息修复</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="dunning:tMisCustomerServiceFeedback:view">
		<li><a href="javascript:void 0;" url="${ctx}/dunning/tMisCustomerServiceFeedback/feedbackList" onclick="childPage(this)">问题反馈</a></li>
	</shiro:hasPermission>
</ul>
<iframe id="ifm" name="ifm" frameborder="0" style="width:100%;height: 500px;"></iframe>
</body>
</html>