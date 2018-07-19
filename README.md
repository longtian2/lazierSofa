# lazierSofa

# 懒人沙发--工具篇

1、为解决接口定义构造JSON字符串、Java对象的痛点，**懒人沙发**实现JSON 字符串生成Java对象，及Java对象生成JSON 字符串。
   运行 JsonAndClassConverter 类的 Main方法，构造入参示例如下：
   
   class C:\Users\chenll\Desktop\cc\json\JsonString.txt C:\Users\chenll\Desktop\cc\json JsonClassRoot r
   
   json C:\Users\chenll\Desktop\cc\json C:\Users\chenll\Desktop\cc\json JsonClassRoot r

      { 
        "id": "主键 Long",
        "name": "姓名 String",
        "age": "年龄 Integer",
        "email": "邮箱 String",
        "mobile": "电话号码 String"
      }
      
  **相互转换**
      
      public class JsonClassRoot { 

		/**
		 * 主键
		 */
		private Long id;

		/**
		 * 姓名
		 */
		private String name;

		/**
		 * 年龄
		 */
		private Integer age;

		/**
		 * 邮箱
		 */
		private String email;

		/**
		 * 电话号码
		 */
		private String mobile;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
	}
