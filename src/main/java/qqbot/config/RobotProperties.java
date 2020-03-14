package qqbot.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "simple-robot.robot")
public class RobotProperties {
	private String commandPrefix;
	private Setu setu;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Setu{
		private Map<String,Object> r18;
		private int setuColdTime = 30;
		private String onSwitch;
		private String offSwitch;
		private boolean enabled;
		private String[] whiteListGroup;
		private String[] whiteListUser;
		
		
//		private static final  Map<String,Object> DEFAULT_R18_CONFIG = new HashMap<>();
//		
//		private static final String GROUP_ENABLED ="groupEnabled";
//		private static final String GROUP_WHITE_LIST_ONLY = "groupWhiteListOnly";
//		private static final String PRIVATE_WHITE_LIST_ONLY = "privateWhiteListOnly";
//		private static final String GROUP_RECALL_TIME = "groupRecallTime";
//		private static final String PRIVATE_RECALL_TIME = "privateRecallTime";
//		
//		static {
//			DEFAULT_R18_CONFIG.put(GROUP_ENABLED, false);
//			DEFAULT_R18_CONFIG.put(GROUP_WHITE_LIST_ONLY, true);
//			DEFAULT_R18_CONFIG.put(PRIVATE_WHITE_LIST_ONLY,true);
//			DEFAULT_R18_CONFIG.put(GROUP_RECALL_TIME, 30);
//			DEFAULT_R18_CONFIG.put(PRIVATE_RECALL_TIME, 0);
//		}
//
//		private R18 r18;
//		@Data
//		@NoArgsConstructor
//		@AllArgsConstructor
//		public static class R18{
//			private boolean groupEnabled;
//			private boolean groupWhiteListOnly;
//			private boolean privateWhiteListOnly;
//			private int groupRecallTime;
//			private int privateRecallTime;
//			
//		}
	}
	
	
}
