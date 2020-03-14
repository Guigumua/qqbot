package qqbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.forte.component.forcoolqhttpapi.CoolQHttpApp;
import com.forte.component.forcoolqhttpapi.CoolQHttpApplication;
import com.forte.component.forcoolqhttpapi.CoolQHttpConfiguration;
import com.forte.qqrobot.depend.DependGetter;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;

import qqbot.service.GroupService;
import qqbot.utils.MsgUtil;

@SpringBootApplication
public class RobotApplication implements CoolQHttpApp {

	public static DependGetter DEPEND_GETTER;
	public static ConfigurableApplicationContext context;
	@Value("${core.port}")
	private int port; 

	public static void main(String[] args) {
		context = SpringApplication.run(RobotApplication.class, args);
		ConfigurableListableBeanFactory factory = context.getBeanFactory();
		@SuppressWarnings("resource")
		CoolQHttpApplication httpApplication = new CoolQHttpApplication();
		// 通过SpringBoot的依赖工厂，构建一个提供给Http Api启动器的依赖工厂，以实现整合
		DEPEND_GETTER = new DependGetter() {

			@Override
			public <T> T get(Class<T> clazz) {
				try {
					return factory.getBean(clazz);
				} catch (Exception e) {
					return null;
				}
			}

			@Override
			public <T> T get(String name, Class<T> type) {
				try {
					return factory.getBean(name, type);
				} catch (Exception e) {
					return null;
				}
			}

			@Override
			public Object get(String name) {
				try {
					return factory.getBean(name);
				} catch (Exception e) {
					return null;
				}
			}

			@Override
			public <T> T constant(String name, Class<T> type) {
				return null;
			}

			@Override
			public Object constant(String name) {
				return null;
			}
		};
		httpApplication.run(new RobotApplication());
	}

	@Override
	public void before(CoolQHttpConfiguration configuration) {
		configuration.setJavaPort(port);
		configuration.setDependGetter(DEPEND_GETTER);
	}

	@Override
	public void after(CQCodeUtil cqCodeUtil, MsgSender sender) {
		GroupService groupService = context.getBean(GroupService.class);
		groupService.findAll().stream().forEach(group ->{
			MsgUtil.send(group.getGroupId(),  sender, "机器人已上线");
		});
	}
}
