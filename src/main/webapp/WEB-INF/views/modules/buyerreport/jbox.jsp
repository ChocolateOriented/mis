<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Content-Language" content="utf-8" />
    <meta name="Robots" content="all" />
    <meta name="Author" content="KudyChen" />
    <meta name="Copyright" content="KudyStudio.Com" />
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />

    <title>JBox Testing</title>
    <link id="skin" rel="stylesheet" href="jBox/Skins2/Pink/jbox.css" />
    <script type="text/javascript" src="jBox/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="jBox/jquery.jBox.src.js"></script>

    <style type="text/css">
        body{ margin:50px;}
        
        .hide-scroll{overflow:hidden;padding-right:17px;}
        div.msg-div{ padding: 10px; }
        div.msg-div p{ padding: 0px; margin:5px 0 0 0; }
        div.msg-div .field{ padding: 0px; }
        div.msg-div .field textarea{ width: 100%; height: 50px; resize:none; font-size:12px; }
        .errorBlock{ background-color: #FFC6A5; border: solid 1px #ff0000; color: #ff0000; margin: 10px 10px 0 10px; padding:7px; font-weight: bold; }
    </style>

    <script type="text/javascript">
        /* jBox 全局设置 */
        var _jBoxConfig = {};
        _jBoxConfig.defaults = {
            id: null, /* 在页面中的唯一ID，如果为null则自动为随机ID,一个ID只会显示一个jBox */
            top: '15%', /* 窗口离顶部的距离,可以是百分比或像素(如 '100px') */
            border: 5, /* 窗口的外边框像素大小,必须是0以上的整数 */
            opacity: 0.2, /* 窗口隔离层的透明度,如果设置为0,则不显示隔离层 */
            timeout: 0, /* 窗口显示多少毫秒后自动关闭,如果设置为0,则不自动关闭 */
            showType: 'fade', /* 窗口显示的类型,可选值有:show、fade、slide */
            showSpeed: 'fast', /* 窗口显示的速度,可选值有:'slow'、'fast'、表示毫秒的整数 */
            showIcon: true, /* 是否显示窗口标题的图标，true显示，false不显示，或自定义的CSS样式类名（以为图标为背景） */
            showClose: true, /* 是否显示窗口右上角的关闭按钮 */
            draggable: true, /* 是否可以拖动窗口 */
            dragLimit: true, /* 在可以拖动窗口的情况下，是否限制在可视范围 */
            dragClone: false, /* 在可以拖动窗口的情况下，鼠标按下时窗口是否克隆窗口 */
            persistent: true, /* 在显示隔离层的情况下，点击隔离层时，是否坚持窗口不关闭 */
            showScrolling: true, /* 是否显示浏览的滚动条 */
            ajaxData: {},  /* 在窗口内容使用post:前缀标识的情况下，ajax post的数据，例如：{ id: 1 } 或 "id=1" */
            iframeScrolling: 'auto', /* 在窗口内容使用iframe:前缀标识的情况下，iframe的scrolling属性值，可选值有：'auto'、'yes'、'no' */

            title: 'jBox', /* 窗口的标题 */
            width: 350, /* 窗口的宽度，值为'auto'或表示像素的整数 */
            height: 'auto', /* 窗口的高度，值为'auto'或表示像素的整数 */
            bottomText: '', /* 窗口的按钮左边的内容，当没有按钮时此设置无效 */
            buttons: { '确定': 'ok' }, /* 窗口的按钮 */
            buttonsFocus: 0, /* 表示第几个按钮为默认按钮，索引从0开始 */
            loaded: function (h) { }, /* 窗口加载完成后执行的函数，需要注意的是，如果是ajax或iframe也是要等加载完http请求才算窗口加载完成，参数h表示窗口内容的jQuery对象 */
            submit: function (v, h, f) { return true; }, /* 点击窗口按钮后的回调函数，返回true时表示关闭窗口，参数有三个，v表示所点的按钮的返回值，h表示窗口内容的jQuery对象，f表示窗口内容里的form表单键值 */
            closed: function () { } /* 窗口关闭后执行的函数 */
        };
        _jBoxConfig.stateDefaults = {
            content: '', /* 状态的内容，不支持前缀标识 */
            buttons: { '确定': 'ok' }, /* 状态的按钮 */
            buttonsFocus: 0, /* 表示第几个按钮为默认按钮，索引从0开始 */
            submit: function (v, h, f) { return true; } /* 点击状态按钮后的回调函数，返回true时表示关闭窗口，参数有三个，v表示所点的按钮的返回值，h表示窗口内容的jQuery对象，f表示窗口内容里的form表单键值 */
        };
        _jBoxConfig.tipDefaults = {
            content: '', /* 提示的内容，不支持前缀标识 */
            icon: 'info', /* 提示的图标，可选值有'info'、'success'、'warning'、'error' */
            top: '40%', /* 提示离顶部的距离,可以是百分比或像素(如 '100px') */
            width: 'auto', /* 提示的高度，值为'auto'或表示像素的整数 */
            height: 'auto', /* 提示的高度，值为'auto'或表示像素的整数 */
            opacity: 0, /* 窗口隔离层的透明度,如果设置为0,则不显示隔离层 */
            timeout: 2000, /* 提示显示多少毫秒后自动关闭,必须是大于0的整数 */
            closed: function () { } /* 提示关闭后执行的函数 */
        };
        _jBoxConfig.messagerDefaults = {
            content: '', /* 信息的内容，不支持前缀标识 */
            title: 'jBox', /* 信息的标题 */
            icon: 'none', /* 信息图标，值为'none'时为不显示图标，可选值有'none'、'info'、'question'、'success'、'warning'、'error' */
            width: 350, /* 信息的高度，值为'auto'或表示像素的整数 */
            height: 'auto', /* 信息的高度，值为'auto'或表示像素的整数 */
            timeout: 3000, /* 信息显示多少毫秒后自动关闭,如果设置为0,则不自动关闭 */
            showType: 'slide', /* 信息显示的类型,可选值有:show、fade、slide */
            showSpeed: 600, /* 信息显示的速度,可选值有:'slow'、'fast'、表示毫秒的整数 */
            border: 0, /* 信息的外边框像素大小,必须是0以上的整数 */
            buttons: {}, /* 信息的按钮 */
            buttonsFocus: 0, /* 表示第几个按钮为默认按钮，索引从0开始 */
            loaded: function (h) { }, /* 窗口加载完成后执行的函数，参数h表示窗口内容的jQuery对象 */
            submit: function (v, h, f) { return true; }, /* 点击信息按钮后的回调函数，返回true时表示关闭窗口，参数有三个，v表示所点的按钮的返回值，h表示窗口内容的jQuery对象，f表示窗口内容里的form表单键值 */
            closed: function () { } /* 信息关闭后执行的函数 */
        };
        _jBoxConfig.languageDefaults = {
            close: '关闭', /* 窗口右上角关闭按钮提示 */
            ok: '确定', /* $.jBox.prompt() 系列方法的“确定”按钮文字 */
            yes: '是', /* $.jBox.warning() 方法的“是”按钮文字 */
            no: '否', /* $.jBox.warning() 方法的“否”按钮文字 */
            cancel: '取消' /* $.jBox.confirm() 和 $.jBox.warning() 方法的“取消”按钮文字 */
        };

        $.jBox.setDefaults(_jBoxConfig);

        function demo00() {
            jBox('id:right-text', { showIcon: 'jbox-title-icon' });
        }
        function demo01() {
            jBox.tip("Hello jBox");
            //jBox.tip("提示关闭后设置输入框为焦点", 'success', { focusId: 'name' });
            //jBox.tip("提示关闭后设置输入框为焦点", 'info', { top: '30%', width: 300, focusId: 'name', closed: function () { alert('before focus input...'); } });
        }
        function demo01_2() {
            jBox.tip("Loading...", 'loading');
            // 模拟2秒后完成操作
            window.setTimeout(function () { jBox.tip('操作成功。', 'success'); }, 2000);
        }
        function demo02() {
            jBox.alert("Hello jBox");
            //jBox.alert("Hello jBox", 'Title', { top: '250px', width: 400, height: 150 });
        }
        function demo03() {
            var submit = function (v, h, f) {
                if (v == true)
                    jBox.tip("恩", 'success');
                else
                    jBox.tip("好吖", 'success');

                return true;
            };

            jBox.confirm("猪，做我女朋友吧？", "表白提示", submit, { id:'hahaha', showScrolling: false, buttons: { '恩': true, '好吖': false } });
        }
        function demo04() {
            jBox.open("iframe:http://www.baidu.com", "百度一下", 800, 350);
        }
        function demo042() {
            jBox.open("iframe:http://www.baidu.com", null, 800, 350, { buttons: { '关闭': true },iframeScrolling: 'no', showClose: false, loaded: function (h) {
                jBox.tip('loaded');
            } 
            });
        }
        function demo043() {
            jBox.open("iframe:http://www.baidu.com", "百度一下", 800, 350, { buttons: { '关闭': true} });
        }
        function demo044() {
            jBox.open("get:ajax.html", null, 'auto', 'auto', { buttons: {} });
        }
        function demo045() {
            jBox.open("get:ajax.html", "Ajax Get", 600, 250, { buttons: {} });  //
        }
        function demo046() {
            jBox.open("get:ajax.html", "Ajax Get", 'auto', 'auto', { buttons: { '关闭': true }, persistent: false });
        }
        function demo05() {
            var html = "<div style='padding:10px;'>输入姓名：<input type='text' id='yourname' name='yourname' value='小明' /></div>";
            var submit = function (v, h, f) {
                if (f.yourname == '') {
                    jBox.tip("请输入您的姓名。", 'error', { focusId: "yourname" }); // 关闭设置 yourname 为焦点
                    return false;
                }

                jBox.tip("你叫：" + f.yourname);
                //jBox.tip("你叫：" + h.find("#yourname").val());
                //jBox.tip("你叫：" + h.find(":input[name='yourname']").val());

                return true;
            };

            jBox(html, { title: "你叫什么名字？", submit: submit, loaded: function (h) {
                //jBox.tip('loaded');
                jBox.tip(h.find("#yourname").val());
            }
            });
        }

        function demo06() {
            var submit = function (v, h, f) {
                if (v == 'yes') {
                    jBox.tip('已保存。');
                }
                if (v == 'no') {
                    jBox.tip('没保存。');
                }
                if (v == 'cancel') {
                    jBox.tip('已取消。');
                }

                return true;
            };

            jBox.warning("内容已修改，是否保存？", "提示", submit);
        }
        function demo07() {
            jBox.messager("messager", "my title", 3000, { width: 250, showType: 'slide', icon: 'info' });
        }
        function demo08() {
            jBox.messager("messager 3", "my title", 3000, { width: 250, showType: 'fade' });
        }
        function demo09() {
            jBox.messager("messager 2", "my title", 3000, { width: 250, showType: 'show' });
        }


        function complex01() {
            var html1 = '<div class="msg-div">' +
                        '<p>购买数量：</p><div class="field"><input type="text" id="amount" name="amount" style="width:75px;" value="1" /></div>' +
                        '<p>收货地址：</p><div class="field"><textarea id="address" name="address"></textarea></div>' +
                        '<div class="errorBlock" style="display: none;"></div>' +
                        '</div>';

            var html2 = '<div class="msg-div">' +
                        '<p>给卖家留言：<span style="color:gray">（选填，可以告诉卖家您对商品的特殊要求）</span></p><div class="field"><textarea id="message" name="message"></textarea></div>' +
                        '</div>';

            var data = {};
            var states = {};
            states.state1 = {
                content: html1,
                buttons: { '下一步': 1, '取消': 0 },
                submit: function (v, h, f) {
                    if (v == 0) {
                        return true; // close the window
                    }
                    else {
                        h.find('.errorBlock').hide('fast', function () { $(this).remove(); });

                        data.amount = f.amount; //或 h.find('#amount').val();
                        if (data.amount == '' || parseInt(data.amount) < 1) {
                            $('<div class="errorBlock" style="display: none;">请输入购买数量！</div>').prependTo(h).show('slow');
                            return false;
                        }
                        data.address = f.address;
                        if (data.address == '') {
                            $('<div class="errorBlock" style="display: none;">请输入收货地址！</div>').prependTo(h).show('slow');
                            return false;
                        }

                        jBox.nextState(); //go forward
                        // 或 jBox.goToState('state2')
                    }

                    return false;
                }
            };
            states.state2 = {
                content: html2,
                buttons: { '上一步': -1, '提交': 1, '取消': 0 },
                buttonsFocus: 1, // focus on the second button
                submit: function (v, o, f) {
                    if (v == 0) {
                        return true; // close the window
                    } else if (v == -1) {
                        jBox.prevState() //go back
                        // 或 jBox.goToState('state1');
                    }
                    else {
                        data.message = f.message;

                        // do ajax request here
                        jBox.nextState('<div class="msg-div">正在提交...</div>');
                        // 或 jBox.goToState('state3', '<div class="msg-div">正在提交...</div>')

                        // asume that the ajax is done, than show the result
                        var msg = [];
                        msg.push('<div class="msg-div">');
                        msg.push('<p>下面是提交的数据</p>');
                        for (var p in data) {
                            msg.push('<p>' + p + ':' + data[p] + '</p>');
                        }
                        msg.push('</div>');
                        window.setTimeout(function () { jBox.nextState(msg.join('')); }, 2000);
                    }

                    return false;
                }
            };
            states.state3 = {
                content: '',
                buttons: { } // no buttons
            };
            states.state4 = {
                content: '',
                buttons: { '确定': 0 }
            };

            jBox.open(states, '提交订单', 450, 'auto');
            // 或 jBox(states, { width: 450, height: 'auto', title: '提交订单' });
        }


        function demo10() {
            var html1 = '<div class="msg-div">' +
                        '<p>购买数量：</p><div class="field"><input type="text" id="amount" name="amount" style="width:75px;" value="1" /></div>' +
                        '<p></p><div class="field"><input type="button" value="提交" /></div>' +
                        '</div>';
            $.jBox.open(html1, "my title", 300, 300, { showType: 'show' });
        }


        $(function () {


        });
        
    </script>


</head>
<body>

<div id="right-text" style="float:right;">
<div style='padding:10px;'>jQuery jBox<br /><br />版本：v2.0<br />日期：2011-7-24<br />官网：<a target="_blank" href="http://kudystudio.com/jbox">http://kudystudio.com/jbox</a></div>
</div>

<!--
<iframe id="jbox-fadedd" class="jbox-fadedd" src="jbox2.html" width="100" height="100"></iframe>
-->

<br /><br />
名字：<input type="text" id="name" />
<select><option>fdss</option></select>
<br /><br />

<input type="button" value="demo00" onclick="demo00();" />
<input type="button" value="demo01" onclick="demo01();" />
<input type="button" value="demo01_2" onclick="demo01_2();" />

<input type="button" value="demo02" onclick="demo02();" />
<input type="button" value="demo03" onclick="demo03();" />
<input type="button" value="demo04" onclick="demo04();" />
<input type="button" value="demo042" onclick="demo042();" />
<input type="button" value="demo043" onclick="demo043();" />
<input type="button" value="demo044" onclick="demo044();" />
<input type="button" value="demo045" onclick="demo045();" />
<input type="button" value="demo046" onclick="demo046();" />
<input type="button" value="demo05" onclick="demo05();" />
<input type="button" value="demo06" onclick="demo06();" />
<br /><br />
<input type="button" value="complex01" onclick="complex01();" />
<br /><br />
<input type="button" value="demo07" onclick="demo07();" />
<input type="button" value="demo08" onclick="demo08();" />
<input type="button" value="demo09" onclick="demo09();" />

<div style="width:100%; height:800px;">&nbsp;</div>

<br /><br />
<!---->
<input type="button" value="demo01_2" onclick="demo01_2();" />
</body>
</html>