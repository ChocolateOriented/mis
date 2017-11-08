<%--
  Created by IntelliJ IDEA.
  User: jxguo
  Date: 2017/10/23
  Time: 17:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>信息</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function() {
            var wechatreg = /^[-_a-zA-Z0-9]{6,20}$/;
            jQuery.validator.addMethod("contactnumber",function(value,element){
                return this.optional(element) || (wechatreg.test(value));
            },"<font color='#E47068'>请输入正确号码格式</font>");
            $('#SaveInformationRecovery').click(function() {
                if($("#inputForm").valid()){
                    $("#SaveInformationRecovery").attr('disabled',"true");
                    $.ajax({
                        type: 'POST',
                        url : "${ctx}/dunning/tMisDunningTask/informationSave",
                        data: $('#inputForm').serialize(),             //获取表单数据
                        success : function(data) {
                            if (data == "OK") {
                                alert("保存成功");
                                //window.parent.window.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
                                window.parent.childPage($("#refresh"));
                                window.parent.window.jBox.close();            //关闭子窗体

                            } else {
                                alert("保存失败:"+data);
                                //window.parent.page();
                                window.parent.window.jBox.close();
                            }
                        },
                        error : function(XMLHttpRequest, textStatus, errorThrown){
                            //通常情况下textStatus和errorThrown只有其中一个包含信息
                            alert("保存失败:"+textStatus);
                        }
                    });
                }
            });
            $('#esc').click(function() {
                window.parent.window.jBox.close();
                });

            $('#UpdateInformationRecovery').click(function() {
                if($("#inputForm").valid()){
                    $("#UpdateInformationRecovery").attr('disabled',"true");
                    $.ajax({
                        type: 'POST',
                        url : "${ctx}/dunning/tMisDunningTask/informationUpdate",
                        data: $('#inputForm').serialize(),             //获取表单数据
                        success : function(data) {
                            if (data == "OK") {
                                alert("修改成功");
                                window.parent.childPage($("#refresh"));
                                window.parent.window.jBox.close();            //关闭子窗体
                            } else {
                                alert("修改失败:"+data);
                                window.parent.window.jBox.close();
                            }
                        },
                        error : function(XMLHttpRequest, textStatus, errorThrown){
                            //通常情况下textStatus和errorThrown只有其中一个包含信息
                            alert("保存失败:"+textStatus);
                        }
                    });
                }
            });
            $('#esc').click(function() {
                window.parent.window.jBox.close();
            });
        });

        function contactNumberClear(){
            $('#contactNumber').val("");
        }
        function contactNameClear() {
            $('#contactName').val("");
        }



    </script>
</head>
<body>
<ul class="nav nav-tabs">

</ul>
<br/>
<form:form id="inputForm" modelAttribute="DunningInformationRecovery"  class="form-horizontal">
    <div class="control-group">
        <div style="width:25%;display:inline-block;text-align:right;">
            <select class="input-small" path="contactType" id="contactType" name="contactType" onchange="contactNumberClear()" required>
                <option value="">类型</option>
                <option value="PHONE" <c:if test="${'PHONE' eq DunningInformationRecovery.contactType}">selected</c:if>>电话</option>
                <option value="WECHAT" <c:if test="${'WECHAT' eq DunningInformationRecovery.contactType}">selected</c:if> >微信 </option>
                <option value="QQ"<c:if test="${'QQ' eq DunningInformationRecovery.contactType}">selected</c:if>>QQ</option>
            </select>
        </div>
        <span class="help-inline"><font color="red">*</font> </span>
        <div style="width:25%;display:inline-block;text-align:right;">
            <label>号码：</label>
        </div>
        <div style="width:35%;display:inline-block;">
            <input  value="${DunningInformationRecovery.contactNumber}" path="contactNumber" id="contactNumber" name="contactNumber" htmlEscape="false" maxlength="20"  required class="contactnumber" />
        </div>
        <span class="help-inline"><font color="red">*</font> </span>
    </div>
    <div class="control-group">
        <div style="width:25%;display:inline-block;text-align:right;">
            <select class="input-small" path="contactRelationship" id="contactRelationship" name="contactRelationship" onchange="contactNameClear()">
                <option value="">关系</option>
                <option value="SELF" <c:if test="${'SELF' eq DunningInformationRecovery.contactRelationship}">selected</c:if>>本人</option>
                <option value="MARRIED" <c:if test="${'MARRIED' eq DunningInformationRecovery.contactRelationship}">selected</c:if> >夫妻 </option>
                <option value="PARENT"<c:if test="${'PARENT' eq DunningInformationRecovery.contactRelationship}">selected</c:if>>父母</option>
                <option value="CHILDREN"<c:if test="${'CHILDREN' eq DunningInformationRecovery.contactRelationship}">selected</c:if>>子女</option>
                <option value="RELATIVES"<c:if test="${'RELATIVES' eq DunningInformationRecovery.contactRelationship}">selected</c:if>>亲戚</option>
                <option value="FRIEND"<c:if test="${'FRIEND' eq DunningInformationRecovery.contactRelationship}">selected</c:if>>朋友</option>
                <option value="WORKMATE"<c:if test="${'WORKMATE' eq DunningInformationRecovery.contactRelationship}">selected</c:if>>同事</option>
                <option value="WORKPHONE"<c:if test="${'WORKPHONE' eq DunningInformationRecovery.contactRelationship}">selected</c:if>>工作电话</option>
            </select>
        </div>
        <div style="width:28%;display:inline-block;text-align:right;">
            <label>姓名：</label>
        </div>
        <div style="width:35%;display:inline-block;text-align:left;">
            <input class="input-small" value="${DunningInformationRecovery.contactName}" path="contactName" id="contactName" name="contactName" htmlEscape="false" maxlength="5"/>
        </div>
    </div>
    <input id="buyerid" type="hidden"  name="buyerid" value="${buyerid}">
    <input id="dealCode" type="hidden"  name="dealCode" value="${dealCode}">
    <input id="id" type="hidden"  name="id" value="${id}">
    <a style="display: none" id="refresh" href="javascript:void 0;" url="${ctx}/dunning/tMisDunningTask/informationRecovery" onclick="childPage(this)"></a>
    <div style= "padding:19px 180px 20px;" >
        <c:if test="${'add' eq method}">
            <input id="SaveInformationRecovery" class="btn btn-primary" type="button" value="保 存"/>&nbsp;
        </c:if>
        <c:if test="${'update' eq method}">
            <input id="UpdateInformationRecovery" class="btn btn-primary" type="button" value="修 改"/>&nbsp;
        </c:if>
        <input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
    </div>
</form:form>
</body>
</html>


