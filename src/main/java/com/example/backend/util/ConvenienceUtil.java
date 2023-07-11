package com.example.backend.util;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.FileCopyUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

public class ConvenienceUtil {

    public static String currentTimestamp() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(currentTimestamp);
    }

    public static String base64EncoderToImg(String fileURL) {
        try {
            Path path = Paths.get(fileURL);
            byte[] fileBytes = Files.readAllBytes(path);
            String base64Data = Base64.getEncoder().encodeToString(fileBytes);
            String mimeType = Files.probeContentType(path);

            return "data:" + mimeType + ";base64," + base64Data;
        } catch (Exception e) {
            throw new CustomException(ErrorType.IMG_ENCODER_FAIL);
        }
    }

    public static BufferedImage base64DecoderToImg(String base64) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] imageByte = decoder.decode(base64);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            BufferedImage image = ImageIO.read(bis);
            bis.close();
            return image;
        } catch (IOException e) {
            throw new CustomException(ErrorType.IMG_CREATE_FAIL);
        }
    }

    public static String getImgPath() {
        return System.getProperty("user.home") + "\\upload\\discord\\frontend\\public";
    }

    public static String makeOrGetChannelFolderURL(int channelUID) {
        String folderURL = getImgPath() + "\\download\\channel\\" + channelUID;
        new File(folderURL).mkdirs();
        return folderURL;
    }

    public static String makeOrGetLobbyFolderURL(String userUID) {
        String folderURL = getImgPath() + "\\download\\lobby\\" + userUID;
        new File(folderURL).mkdirs();
        return folderURL;
    }

    public static void jwtExceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String currentTimestampToString = ConvenienceUtil.currentTimestamp();
        Map<String, Object> exceptionInfo = ErrorType.findErrorTypeByMessage(e.getMessage());
        ObjectMapper om = new ObjectMapper();
        ObjectNode responseJson = om.createObjectNode();
        responseJson.put("timestamp", currentTimestampToString);
        responseJson.put("status", (int) exceptionInfo.get("status"));
        responseJson.put("error", (String) exceptionInfo.get("error"));
        responseJson.put("code", ((ErrorType) exceptionInfo.get("code")).name());
        responseJson.put("message", e.getMessage());
        responseJson.put("details", request.getRequestURI());

        String formattedJson = om.writer().withDefaultPrettyPrinter().writeValueAsString(responseJson);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json"); // JSON 형식으로 설정
        response.setStatus((int) exceptionInfo.get("status"));
        response.getWriter().write(formattedJson);
    }
}
