<%--
  Created by IntelliJ IDEA.
  User: heyon
  Date: 2018/1/23
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";

    String menuId = (String) request.getAttribute("menuId");
    String type = (String) request.getAttribute("type");
%>
<!--_meta 作为公共模版分离出去-->
<!DOCTYPE HTML>
<html>
<head>
    <base href="<%=basePath%>"/>
    <jsp:include page="../../common/top.jsp"/>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="Bookmark" href="/favicon.ico">
    <link rel="Shortcut Icon" href="/favicon.ico"/>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="lib/html5shiv.js"></script>
    <script type="text/javascript" src="lib/respond.min.js"></script>

    <![endif]-->
    <link rel="stylesheet" type="text/css" href="static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" type="text/css" href="lib/Hui-iconfont/1.0.8/iconfont.css"/>

    <link rel="stylesheet" type="text/css" href="static/h-ui.admin/skin/default/skin.css" id="skin"/>
    <link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/style.css"/>
    <!--[if IE 6]>
    <script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js"></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <!--/meta 作为公共模版分离出去-->
</head>
<body>
<div class="page-container">

    <form class="form form-horizontal" id="form-menu-info">
        <input id="pageInfo" type="text" class="<%=type%>" name="id" value="<%=menuId%>" style="display: none"/>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>菜单名称：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="menuName" name="name"/>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2"><span class="c-red"></span>父级菜单：</label>
            <%--用于查看信息--%>
            <div class="formControls col-xs-8 col-sm-9 prentInput">
                <input type="text" class="input-text" value="" placeholder="" id="prentStr" name="prentStr"/>
            </div>
            <%--用于修改添加--%>
            <div class="formControls col-xs-8 col-sm-9 prentSelect">
                <span class="select-box">
                    <select id="selectId" name="parentId" class="select">
                        <option value="0" selected="selected" >-</option>
                    </select>
				</span>
            </div>
        </div>


        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">排序值：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="0" placeholder="" id="seq" name="seq">
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">url：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="" placeholder="" id="url" name="url"/>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">图标：</label>
            <div class="formControls col-xs-8 col-sm-9"> <span class="btn-upload form-group">
                <input id="icons" class="input-text" type="text" name="icon" readonly="readonly"  style="width:200px">
                 <a class="btn btn-primary radius iconBtn" onclick="initIcon()" href="javascript:;">
                    <i class="Hui-iconfont">&#xe642;</i> 查看图标</a></span>
                </span> <i class="iconImg Hui-iconfont"></i></div>
        </div>

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-2">备注：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <textarea id="textareaDiv" name="remark" class="textarea" maxlength="200"></textarea>
            </div>
        </div>

        <div class="row cl">
            <div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
                <input class="btn btn-primary radius submitBtn" type="button" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
            </div>
        </div>
    </form>
</div>

</body>
</html>

<%-- 点击提交按钮 --%>
<script type="text/javascript">
    $(".submitBtn").click(function () {
        $.ajax({
            url:"sys/menu/update.json",
            data: $("#form-menu-info").serializeArray(),
            type:"post",
            success:function (result) {
                if(result.ret){
                    layer.msg('修改成功', {icon: 1});
                }else{
                    layer.msg(result.msg, {icon: 5});
                }
            }
        });
    })
</script>


<!-- 界面初始化 -->
<script type="text/javascript">

    // 首先判断是什么类型，如果是展示信息，那么就不要提交按钮，并且所有input不可被编辑
    // 如果是添加，界面没有任何值，并且显示提交按钮
    // 如果是修改，展示信息，并且显示提交按钮
    // 1. 先判断是什么类型
    var type = $("#pageInfo").attr("class");
    var menuId = $("#pageInfo").val();
    var inputs = $("input");
    if (type == "info") {
        MenuInfo(menuId);
    } else if (type == "update") {
        MenuUpdate(menuId);
    } else if (type == "add") {
        MenuAdd();
    }

    /*添加*/
    function MenuAdd() {

    }


    /* 修改信息 */
    function MenuUpdate(menuId) {
        // $("#prentStr").css("display", "none");
        $(".prentSelect").css("display", "none");
        $("#prentStr").attr("readonly", "readonly");
        initMenuInfo(menuId,"update");
    }


    // 查看菜单信息
    function MenuInfo(menuId) {
        $(".submitBtn").css("display", "none");
        $(".prentSelect").css("display", "none");
        $(".iconBtn").css("display", "none");
        $(inputs).each(function (i, input) {
            $(this).attr("readonly", "readonly");
        })
        $("#textareaDiv").attr("readonly", "readonly");
        if (menuId != -1) {
            initMenuInfo(menuId,"info");
        }
    }

    function initIcon(){
        layer.open({
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: 'sys/menu/toIconPage'
        });
    }

    // 初始化树形下拉菜单,只会加载一级菜单
    // selectId,初始化要选中的值没有传-1
    function initSelect(selectId) {
        $.ajax({
            url: "sys/index/initMenuTree",
            success: function (result) {
                if (result.ret) {
                    var data = result.data;
                    $(data).each(function (i, menu) {
                        $("#selectId").append("<option class='option_"+menu.id+"'  value='" + menu.id + "'>"  + menu.name + "</option>");
                    })
                    $(".option_" + selectId).attr("selected","selected");
                } else {
                    alert("初始化下拉菜单，信息加载失败");
                }
            }
        });
    }

    /*初始化信息*/
    function initMenuInfo(menuId,type) {
        $.ajax({
            url: "sys/menu/select?id=" + menuId,
            success: function (result) {
                if (result.ret) {
                    $("#menuName").val(result.data.name);
                    if(type == "add"){
                        initSelect(result.data.parentId);
                    }else{
                        $("#prentStr").val(result.data.parentStr);
                    }

                    $("#seq").val(result.data.seq);
                    if (result.data.icon != "") {
                        $("#icons").val(result.data.icon);
                        $(".iconImg").html("&" + result.data.icon);
                    }

                    $("#textareaDiv").val(result.data.remark);
                    $("#url").val(result.data.url);
                } else {
                    layer.msg('信息加载失败', {icon: 5});
                }
            }
        });
    }
</script>

