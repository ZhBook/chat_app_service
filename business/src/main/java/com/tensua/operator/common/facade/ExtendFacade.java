package com.tensua.operator.common.facade;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.awt.*;

/**
 * @author zhooke
 * @since 2021/12/31 13:55
 **/
@Service
public class ExtendFacade {

    /**
     * 获取二维码
     * @param content
     * @return
     */
    public byte[] getQrCode(String content) {
        QrConfig config = new QrConfig(300, 300);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.CYAN);
        // 设置背景色（灰色）
        config.setBackColor(Color.GRAY);
        // 高纠错级别
        config.setErrorCorrection(ErrorCorrectionLevel.H);
        // 生成二维码到文件，也可以到流
        return QrCodeUtil.generatePng(content, config);
    }
}
