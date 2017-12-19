package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.entity.DunningMaxRepayNumberAndPrincipal;
import com.mo9.risk.modules.dunning.entity.SMisDunningTaskMonthReport;
import com.mo9.risk.modules.dunning.service.TMisDunningMaxRepayNumberAndPrincipalService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * Created by jxguo on 2017/12/12.
 */
@Controller
@RequestMapping(value = "${adminPath}/dunning/tMisDunningMaxRepayNumberAndPrincipalReport")
public class TMisDunningMaxRepayNumberAndPrincipalController extends BaseController {

    @Autowired
    private TMisDunningMaxRepayNumberAndPrincipalService tMisDunningMaxRepayNumberAndPrincipalService;

    @RequestMapping(value = "personalMaxRepayNumberAndPrincipal")
    @RequiresPermissions("dunning:tMisDunningMaxRepayNumberAndPrincipalReport:view")
    public String getPersonalMaxRepayNumberAndPrincipalofDay(DunningMaxRepayNumberAndPrincipal entity, HttpServletRequest request,
                                                             HttpServletResponse response, Model model){
        if (entity.getDateTime() == null){
            return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofDay";
        }
        Page<DunningMaxRepayNumberAndPrincipal> page = tMisDunningMaxRepayNumberAndPrincipalService
                .getPersonalMaxRepayNumberAndPrincipalPageofDay(new Page<DunningMaxRepayNumberAndPrincipal>(request,response), entity);
        model.addAttribute("page", page);
        model.addAttribute("datetime", entity.getDateTime());
        return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofDay";
    }


    @RequestMapping(value = "groupMaxRepayNumberAndPrincipalofDay")
    @RequiresPermissions("dunning:tMisDunningMaxRepayNumberAndPrincipalReport:view")
    public String getGroupMaxRepayNumberAndPrincipalofDay(DunningMaxRepayNumberAndPrincipal entity, HttpServletRequest request,
                                                          HttpServletResponse response, Model model){
        if (entity.getDateTime() == null){
            return "modules/dunning/sMisGroupMaxRepayNumberAndPrincipalofDay";
        }
        Page<DunningMaxRepayNumberAndPrincipal> page = tMisDunningMaxRepayNumberAndPrincipalService
                .getGroupMaxRepayNumberAndPrincipalPageofDay(new Page<DunningMaxRepayNumberAndPrincipal>(request,response), entity);
        model.addAttribute("page", page);
        model.addAttribute("datetime", entity.getDateTime());
        return "modules/dunning/sMisGroupMaxRepayNumberAndPrincipalofDay";
    }

    @RequestMapping(value = "personalMaxRepayNumberAndPrincipalofPeriod")
    @RequiresPermissions("dunning:tMisDunningMaxRepayNumberAndPrincipalReport:view")
    public String getPersonalMaxRepayNumberAndPrincipalofPeriod(DunningMaxRepayNumberAndPrincipal entity, HttpServletRequest request,
                                                                HttpServletResponse response, Model model){
        if (entity.getDateTime() == null){
            DunningMaxRepayNumberAndPrincipal dunningMaxRepayNumberAndPrincipal = new DunningMaxRepayNumberAndPrincipal();
                new ModelAndView("sMisPersonalMaxRepayNumberAndPrincipalofPeriod").addObject(dunningMaxRepayNumberAndPrincipal);
            return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofPeriod";
        }
        Page<DunningMaxRepayNumberAndPrincipal> page = tMisDunningMaxRepayNumberAndPrincipalService
                .getPersonalMaxRepayNumberAndPrincipalPageofPeriod(new Page<DunningMaxRepayNumberAndPrincipal>(request,response), entity);
        model.addAttribute("page", page);
        model.addAttribute("datetime",entity.getDateTime());
        model.addAttribute("monthdesc", entity.getMonthdesc());
        return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofPeriod";
    }

    @RequestMapping(value = "groupMaxRepayNumberAndPrincipalofPeriod")
    @RequiresPermissions("dunning:tMisDunningMaxRepayNumberAndPrincipalReport:view")
    public String getGroupMaxRepayNumberAndPrincipalListofPeriod(DunningMaxRepayNumberAndPrincipal entity, HttpServletRequest request,
                                                                 HttpServletResponse response, Model model){
        if (entity.getDateTime() == null){
            DunningMaxRepayNumberAndPrincipal dunningMaxRepayNumberAndPrincipal = new DunningMaxRepayNumberAndPrincipal();
            new ModelAndView("sMisPersonalMaxRepayNumberAndPrincipalofPeriod").addObject(dunningMaxRepayNumberAndPrincipal);
            return "modules/dunning/sMisGroupMaxRepayNumberAndPrincipalofPeriod";
        }
        Page<DunningMaxRepayNumberAndPrincipal> page = tMisDunningMaxRepayNumberAndPrincipalService
                .getGroupMaxRepayNumberAndPrincipalPageofPeriod(new Page<DunningMaxRepayNumberAndPrincipal>(request,response), entity);
        model.addAttribute("page", page);
        model.addAttribute("datetime",entity.getDateTime());
        model.addAttribute("monthdesc", entity.getMonthdesc());
        return "modules/dunning/sMisGroupMaxRepayNumberAndPrincipalofPeriod";
    }


    @RequestMapping(value = "exportPersonalMaxRepayNumberAndPrincipal")
    @RequiresPermissions("dunning:tMisDunningMaxRepayNumberAndPrincipalReport:view")
    public String exportPersonalMaxRepayNumberAndPrincipalofDay(DunningMaxRepayNumberAndPrincipal entity, HttpServletRequest request,
                                                             HttpServletResponse response, RedirectAttributes redirectAttributes){
        if (entity.getDateTime() == null){
            return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofDay";
        }
        try {
            String fileName = "个人日还款最大户数&本金.xlsx";
            List<DunningMaxRepayNumberAndPrincipal> page = tMisDunningMaxRepayNumberAndPrincipalService
                    .exportPersonalMaxRepayNumberAndPrincipalListofDay(entity);
            new ExportExcel("个人日还款最大户数&本金", DunningMaxRepayNumberAndPrincipal.class).setDataList(page).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
        }
        return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofDay";
    }

    @RequestMapping(value = "exportGroupMaxRepayNumberAndPrincipal")
    @RequiresPermissions("dunning:tMisDunningMaxRepayNumberAndPrincipalReport:view")
    public String exportGroupMaxRepayNumberAndPrincipalofDay(DunningMaxRepayNumberAndPrincipal entity, HttpServletRequest request,
                                                                HttpServletResponse response, RedirectAttributes redirectAttributes){
        if (entity.getDateTime() == null){
            return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofDay";
        }
        try {
            String fileName = "组日还款最大户数&本金.xlsx";
            List<DunningMaxRepayNumberAndPrincipal> page = tMisDunningMaxRepayNumberAndPrincipalService
                    .exportGroupMaxRepayNumberAndPrincipalListofDay(entity);
            new ExportExcel("组日还款最大户数&本金", DunningMaxRepayNumberAndPrincipal.class).setDataList(page).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
        }
        return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofDay";
    }

    @RequestMapping(value = "exportPersonalMaxRepayNumberAndPrincipalofPeriod")
    @RequiresPermissions("dunning:tMisDunningMaxRepayNumberAndPrincipalReport:view")
    public String exportPersonalMaxRepayNumberAndPrincipalofPeriod(DunningMaxRepayNumberAndPrincipal entity, HttpServletRequest request,
                                                             HttpServletResponse response, RedirectAttributes redirectAttributes){
        if (entity.getDateTime() == null){
            return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofDay";
        }
        try {
            String fileName = "个人周期还款最大户数&本金.xlsx";
            List<DunningMaxRepayNumberAndPrincipal> page = tMisDunningMaxRepayNumberAndPrincipalService
                    .exportPersonalMaxRepayNumberAndPrincipalListofPeriod(entity);
            new ExportExcel("个人周期还款最大户数&本金", DunningMaxRepayNumberAndPrincipal.class).setDataList(page).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
        }
        return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofPeriod";
    }

    @RequestMapping(value = "exportGroupMaxRepayNumberAndPrincipalListofPeriod")
    @RequiresPermissions("dunning:tMisDunningMaxRepayNumberAndPrincipalReport:view")
    public String exportGroupMaxRepayNumberAndPrincipalListofPeriod(DunningMaxRepayNumberAndPrincipal entity, HttpServletRequest request,
                                                                   HttpServletResponse response, RedirectAttributes redirectAttributes){
        if (entity.getDateTime() == null){
            return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofDay";
        }
        try {
            String fileName = "组周期还款最大户数&本金.xlsx";
            List<DunningMaxRepayNumberAndPrincipal> page = tMisDunningMaxRepayNumberAndPrincipalService
                    .exportGroupMaxRepayNumberAndPrincipalListofPeriod(entity);
            new ExportExcel("组周期还款最大户数&本金", DunningMaxRepayNumberAndPrincipal.class).setDataList(page).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出失败！失败信息："+e.getMessage());
        }
        return "modules/dunning/sMisPersonalMaxRepayNumberAndPrincipalofPeriod";
    }

}
