package com.furongsoft.agv.controllers;

import com.alibaba.fastjson.JSON;
import com.furongsoft.agv.models.CallMaterialModel;
import com.furongsoft.agv.models.CallResponeModel;
import com.furongsoft.agv.models.WaveDetailModel;
import com.furongsoft.agv.services.CallMaterialService;
import com.furongsoft.base.misc.Tracker;
import com.furongsoft.base.restful.entities.RestResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 叫料控制层
 *
 * @author linyehai
 */
@RestController
@RequestMapping("/api/v1/agv")
public class CallMaterialController {
    private final CallMaterialService callMaterialService;

    public CallMaterialController(CallMaterialService callMaterialService) {
        this.callMaterialService = callMaterialService;
    }

    /**
     * 根据条件获取叫料列表
     *
     * @param type          类型
     * @param state         状态
     * @param teamId        班组唯一标识
     * @param areaId        区域ID（产线ID）
     * @param siteId        站点ID
     * @param areaCoding    区域编码 （3B、3C）
     * @param productLine   生产线
     * @param executionTime 执行日期
     * @return 响应内容
     */
    @GetMapping("/callMaterials")
    public RestResponse getCallMaterials(int type, @RequestParam(required = false) Integer state, @RequestParam(required = false) String teamId, @RequestParam(required = false) Long areaId, @RequestParam(required = false) Long siteId, String areaCoding, @RequestParam(required = false) String productLine, @RequestParam(required = false) String executionTime) {
        return new RestResponse(HttpStatus.OK, null, callMaterialService.selectCallMaterialsByConditions(type, state, teamId, areaId, siteId, areaCoding, productLine, executionTime));
    }

    /**
     * 按条件查询配货任务
     *
     * @param type          叫料类型[1：灌装区；2：包装区；3：消毒间；4：拆包间]
     * @param state         状态[1：未配送；2：配送中；3：已完成]
     * @param teamId        班组唯一标识
     * @param areaId        区域ID（产线ID）
     * @param siteId        站点ID
     * @param areaCoding    区域编码 （3B、3C）
     * @param productLine   生产线
     * @param executionTime 执行日期
     * @return 响应内容
     */
    @GetMapping("/callMaterials/distributionTasks")
    public RestResponse selectDistributionTaskByConditions(int type, @RequestParam(required = false) Integer state, @RequestParam(required = false) String teamId, @RequestParam(required = false) Long areaId, @RequestParam(required = false) Long siteId, String areaCoding, @RequestParam(required = false) String productLine, @RequestParam(required = false) String executionTime) {
        return new RestResponse(HttpStatus.OK, null, callMaterialService.selectDistributionTaskByConditions(type, state, teamId, areaId, siteId, areaCoding, productLine, executionTime));
    }

    /**
     * 获取仓库任务
     *
     * @param areaCoding    区域编码（3B、3C）
     * @param productLine   生产线（产线ID）
     * @param executionTime 生产日期
     * @return 响应内容
     */
    @GetMapping("/callMaterials/selectWarehouseTask")
    public RestResponse selectWarehouseTask(String areaCoding, @RequestParam(required = false) String productLine, @RequestParam(required = false) String executionTime) {
        return new RestResponse(HttpStatus.OK, null, callMaterialService.selectWarehouseTask(areaCoding, productLine, executionTime));
    }

    /**
     * 根据叫料ID获取叫料信息
     *
     * @param id 叫料ID
     * @return 响应内容
     */
    @GetMapping("/callMaterials/{id}")
    public RestResponse getCallMaterials(@NonNull @PathVariable Long id) {
        return new RestResponse(HttpStatus.OK, null, callMaterialService.selectCallMaterialById(id));
    }

    /**
     * 新增叫料信息
     *
     * @param callMaterialModel 叫料信息
     * @return 响应内容
     */
    @PostMapping("/callMaterials")
    public RestResponse addCallMaterial(@NonNull @RequestBody CallMaterialModel callMaterialModel) {
        callMaterialService.addCallMaterial(callMaterialModel);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 添加波次详情叫料
     *
     * @param waveDetailModels 波次详情列表
     * @param areaCoding       区域编号（3B、3C）
     * @param areaType         "DISINFECTION"：消毒间、"UNPACKING"：拆包间
     * @return 响应内容
     */
    @PostMapping("/callMaterials/addWaveDetailCallMaterials")
    public RestResponse addWaveDetailCallMaterials(@RequestBody List<WaveDetailModel> waveDetailModels, String areaCoding, String areaType) {
        callMaterialService.addWaveDetailCallMaterials(waveDetailModels, areaCoding, areaType);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 删除叫料信息
     *
     * @param id 叫料ID
     * @return 响应内容
     */
    @DeleteMapping("/callMaterials/{id}")
    public RestResponse deleteCallMaterial(@NonNull @PathVariable Long id) {
        return new RestResponse(HttpStatus.OK, null, callMaterialService.deleteCallMaterial(id));
    }

    /**
     * 通过叫料ID取消叫料
     *
     * @param id 叫料ID
     * @return 响应内容
     */
    @DeleteMapping("/callMaterials/cancel/{id}")
    public RestResponse cancelCallMaterial(@NonNull @PathVariable Long id) {
        callMaterialService.cancelCallMaterial(id);
        return new RestResponse(HttpStatus.OK);
    }

    /**
     * 通过波次取消叫料
     *
     * @param waveCode 波次编号
     * @return 响应内容
     */
    @PostMapping("/callMaterials/cancelWave")
    public RestResponse cancelWaveCallMaterials(@RequestParam String waveCode) {
        callMaterialService.cancelWaveCallMaterials(waveCode);
        return new RestResponse(HttpStatus.OK);
    }

    @GetMapping("/callMaterials/button")
    public String callMaterialByButtonBox(HttpServletRequest httpServletRequest, @RequestParam(required = false) String ipAdddress, @RequestParam(required = false) String deviceKey, @RequestParam(required = false) String codingFormat, String buttonCode) {
        System.out.println("ip_address:" + ipAdddress);
        System.out.println("deviceKey:" + deviceKey);
        System.out.println("buttonCode:" + buttonCode);
        String requestIP = getRequestIp(httpServletRequest);
        System.out.println(requestIP);
        Tracker.agv(String.format("ip_address:%s \n deviceKey:%s \n buttonCode:%s \n requestIP:%s ", ipAdddress, deviceKey, buttonCode));
        CallResponeModel callResponeModel;
        String message = callMaterialService.callMaterialByButtonBox(ipAdddress, requestIP, deviceKey, buttonCode);
        if (StringUtils.isEmpty(codingFormat) || "unicode".equalsIgnoreCase(codingFormat)) {
            String base64;
            byte[] unicodeChars = string2Unicode(message);
            base64 = Base64Utils.encodeToString(unicodeChars);
//            if (buttonCode.equalsIgnoreCase("1")) {
//                message = "success";
//                byte[] unicodeChars = string2Unicode(message);
//                for (int i = 0; i < unicodeChars.length; i++) {
//                    System.out.println(unicodeChars[i]);
//                }
//                base64 = Base64Utils.encodeToString(unicodeChars);
//                System.out.println(base64);
//            } else if (buttonCode.equalsIgnoreCase("2")) {
//                message = "发货失败，目标区域无空库位。";
//                byte[] unicodeChars = string2Unicode(message);
//                for (int i = 0; i < unicodeChars.length; i++) {
//                    System.out.println(unicodeChars[i]);
//                }
//                base64 = Base64Utils.encodeToString(unicodeChars);
//                System.out.println(base64);
//            } else if (buttonCode.equalsIgnoreCase("3")) {
//                message = "叫料失败，无未叫料波次";
//                byte[] unicodeChars = string2Unicode(message);
//                for (int i = 0; i < unicodeChars.length; i++) {
//                    System.out.println(unicodeChars[i]);
//                }
//                base64 = Base64Utils.encodeToString(unicodeChars);
//                System.out.println(base64);
//            } else {
//                message = "叫料失败，存在未配送叫料";
//                byte[] unicodeChars = string2Unicode(message);
//                for (int i = 0; i < unicodeChars.length; i++) {
//                    System.out.println(unicodeChars[i]);
//                }
//                base64 = Base64Utils.encodeToString(unicodeChars);
//                System.out.println(base64);
//            }
            callResponeModel = new CallResponeModel("success".equalsIgnoreCase(message) ? 0 : -1, base64);
        } else {
            callResponeModel = new CallResponeModel("success".equalsIgnoreCase(message) ? 0 : -1, message);
        }

        return JSON.toJSONString(callResponeModel);
    }

    /**
     * 获取仓库任务
     *
     * @return 响应内容
     */
    @GetMapping("/heart")
    public String heart(HttpServletRequest httpServletRequest) {
        String ip = getRequestIp(httpServletRequest);
        System.out.println("请求者IP地址：`${ip}`:"+ip);
        String message = "这是心跳";
        // 字符串转Unicode字节数组；转base64编码字符串
        byte[] unicodeChars = string2Unicode(message);
        String base64 = Base64Utils.encodeToString(unicodeChars);
        CallResponeModel callResponeModel = new CallResponeModel(0, base64);
        return JSON.toJSONString(callResponeModel);
    }

    /**
     * 获取请求者的IP地址
     *
     * @param httpServletRequest http请求对象
     * @return IP地址
     */
    private String getRequestIp(HttpServletRequest httpServletRequest) {
        String requestIP = httpServletRequest.getHeader("x-forwarded-for");
        if (requestIP == null || requestIP.length() == 0 || "unknown".equalsIgnoreCase(requestIP)) {
            requestIP = httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (requestIP == null || requestIP.length() == 0 || "unknown".equalsIgnoreCase(requestIP)) {
            requestIP = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (requestIP == null || requestIP.length() == 0 || "unknown".equalsIgnoreCase(requestIP)) {
            requestIP = httpServletRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (requestIP == null || requestIP.length() == 0 || "unknown".equalsIgnoreCase(requestIP)) {
            requestIP = httpServletRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (requestIP == null || requestIP.length() == 0 || "unknown".equalsIgnoreCase(requestIP)) {
            requestIP = httpServletRequest.getRemoteAddr();
        }
        return requestIP;
    }

    /**
     * 字符串转Unicode编码字节数组
     *
     * @param src 字符串
     * @return 字节数组
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    private byte[] string2Unicode(String src)
            throws NullPointerException, IllegalArgumentException {
        if (src == null) {
            throw new NullPointerException("转码内容为空！");
        }

        // 定义常量: 空格
        final String SPACE = " ";
        // 定义常量: 16 进制
        final int RADIX_HEX = 16;

        if ("".equals(src)) {
            src = src + SPACE;
        }

        // 字符串长度
        final int stringLength = src.length();
        // 字符串转换为字符数组
        char[] utfChar = src.toCharArray();
        String hexStr = "";
        // 返回值（unicode字符编码是两个字节）
        byte[] result = new byte[stringLength * 2];
//        byte[] result = new byte[stringLength * 4];

        for (int i = 0; i < stringLength; i++) {
            // 转换为16进制
            hexStr = Integer.toHexString(utfChar[i]);
            // 英文字符需要在前面补加数字 00
            if (hexStr.length() > 1 && hexStr.length() <= 2) {
                hexStr = "00" + hexStr;
            } else if (hexStr.length() == 4) {
            } else { // 如果转义字符写错（如\t应该是\\t），报此异常
                throw new IllegalArgumentException(
                        "转码内容有误，请确认转码内容是否正确");
            }
            System.out.println("hexStr:" + hexStr);
            // 截取字符串
//            char[] unicodeChars = hexStr.toCharArray();
            String head = hexStr.substring(0, 2);
            String tail = hexStr.substring(2);
            try {
                // 16进制数转换为byte   String.valueOf(unicodeChars[i])
                result[i * 2] = (byte) Integer.parseUnsignedInt(head, RADIX_HEX);
                result[i * 2 + 1] = (byte) Integer.parseUnsignedInt(tail, RADIX_HEX);
//                result[i * 4] = (byte) Integer.parseUnsignedInt(String.valueOf(unicodeChars[0]), RADIX_HEX);
//                result[i * 4 + 1] = (byte) Integer.parseUnsignedInt(String.valueOf(unicodeChars[1]), RADIX_HEX);
//                result[i * 4 + 2] = (byte) Integer.parseUnsignedInt(String.valueOf(unicodeChars[2]), RADIX_HEX);
//                result[i * 4 + 3] = (byte) Integer.parseUnsignedInt(String.valueOf(unicodeChars[3]), RADIX_HEX);
            } catch (NumberFormatException nfe) {
                continue;
            }
        }

        return result;
    }

}
