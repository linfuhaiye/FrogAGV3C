package com.furongsoft.base.misc;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.furongsoft.base.exceptions.BaseException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * excel 工具类
 *
 * @author liujianning
 */
public class ExcelUtils {

    private final static String[] imageTypes = {".xls", ".xlsx"};

    /**
     * 带标题、sheet名称导出
     *
     * @param list      数据列表
     * @param title     标题
     * @param sheetName sheet名称
     * @param pojoClass 对应对象类
     * @param fileName  导出文件名称
     * @param response  响应对象
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }

    /**
     * map 形式导出
     *
     * @param list     数据列表
     * @param fileName 导出文件名
     * @param response 响应对象
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
        defaultExport(list, fileName, response);
    }

    /**
     * excel 导出（带对应实体类）
     *
     * @param list         数据列表
     * @param pojoClass    对应对象类
     * @param fileName     导出文件名
     * @param response     响应对象
     * @param exportParams 导出配置
     */
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    /**
     * excel 导入 （具体文件）
     *
     * @param filePath   文件路径
     * @param titleRows  标题行数
     * @param headerRows 头部行数
     * @param pojoClass  对应实体类
     * @param <T>
     * @return 数据列表
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (StringUtils.isNullOrEmpty(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new BaseException("模板不能为空");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(e.getMessage());
        }
        return list;
    }

    /**
     * excel 导入 （上传文件）
     *
     * @param file       文件对象
     * @param titleRows  标题行数
     * @param headerRows 头部行数
     * @param pojoClass  对应实体类
     * @param <T>
     * @return 数据列表
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (file == null) {
            return null;
        }
        String fileName = file.getOriginalFilename().toLowerCase();
        if (StringUtils.isNullOrEmpty(fileName)) {
            throw new BaseException("上传文件名不能为空/后缀名为空");
        }
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isNullOrEmpty(suffixName)) {
            throw new BaseException("上传文件名不能为空/后缀名为空");
        }

        Boolean check = false;
        for (String type : imageTypes) {
            if (type.equals(suffixName)) {
                check = true;
                break;
            }
        }

        if (!check) {
            throw new BaseException("文件上传类型不正确。");
        }

        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new BaseException("excel文件不能为空");
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
        return list;
    }

    /**
     * excel 下载
     *
     * @param fileName 文件名称
     * @param response 响应对象
     * @param workbook excel工作薄对象
     */
    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }
    }

    /**
     * excel 导出（map）
     *
     * @param list     数据列表
     * @param fileName 文件名
     * @param response 响应对象
     */
    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }

    }
}
