package com.tensua.config;

import cn.hutool.core.date.DateUtil;
import com.tensua.component.PushComponent;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * 自定义通知
 * 继承 AbstractStatusChangeNotifier 类，实现了 doNotify 方法，
 * 当应用状态改变的时候会回调 doNotify 方法。
 */
@Slf4j
@Component
public class CustomNotifierConfig extends AbstractStatusChangeNotifier {
    //
    @Autowired
    private PushComponent pushComponent;

    public CustomNotifierConfig(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                log.info("实例名称：" + instance.getRegistration().getName());
                log.info("实例服务地址：" + instance.getRegistration().getServiceUrl());
                String status = ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus();
                StatusChangedEventEnum eventEnum = StatusChangedEventEnum.getEnumByCode(status);
                log.info(eventEnum.desc);
                switch (eventEnum) {
                    case UP:
                        publishNotice(instance, String.format("<font color=\"#00FF00\">%s</font>", eventEnum.desc));
                        break;
                    case DOWN:
                    case OFFLINE:
                    case UNKNOWN:
                    default:
                        publishNotice(instance, String.format("<font color=\"#FF0000\">%s</font>", eventEnum.desc));
                        break;
                }
            }
        });
    }

    private void publishNotice(Instance instance, String status) {
        String now = DateUtil.formatDateTime(new Date());
        String msg = String.format("### 监控通知\n > **服务名称：%s** \n\n> **状态：**%s \n\n> **时间：%s**", instance.getRegistration().getName(), status, now);
        pushComponent.pushDingTalk("监控通知", msg);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public enum StatusChangedEventEnum {

        DOWN("健康检查没通过"),
        OFFLINE("服务离线"),
        UP("服务上线"),
        UNKNOWN("服务未知异常"),
        ;
        String desc;

        public static StatusChangedEventEnum getEnumByCode(String name) {
            for (StatusChangedEventEnum eventEnum : StatusChangedEventEnum.values()) {
                if (eventEnum.name().equals(name)) {
                    return eventEnum;
                }
            }
            return UNKNOWN;
        }
    }
}