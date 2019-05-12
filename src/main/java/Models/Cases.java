package Models;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import io.jsondb.annotation.Secret;

@Document(collection = "Cases", schemaVersion= "1.0")
public class Cases {
  //This field will be used as a primary key, every POJO should have one
  @Id
  private String id;
  
  private String testScreenId;
  private String testScreenName;
  private int groupNo;
  private int subGroupNo;
  private int no;
  private String fieldName;
  private String Type;
  private int numberOfDigit;
  private String value;
  
  public Cases() {}
  public Cases(String testScreenId, String testScreenName, int groupNo, int subGroupNo, int no, String fieldName, String Type, 
		  int numberOfDigit, String value) {
	  this.testScreenId = testScreenId;
	  this.testScreenName = testScreenName;
	  this.groupNo = groupNo;
	  this.subGroupNo = subGroupNo;
	  this.no = no;
	  this.fieldName = fieldName;
	  this.Type = Type;
	  this.numberOfDigit = numberOfDigit;
	  this.value = value;
  }
  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTestScreenId() {
		return testScreenId;
	}
	public void setTestScreenId(String testScreenId) {
		this.testScreenId = testScreenId;
	}
	public String getTestScreenName() {
		return testScreenName;
	}
	public void setTestScreenName(String testScreenName) {
		this.testScreenName = testScreenName;
	}
	public int getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}
	public int getSubGroupNo() {
		return subGroupNo;
	}
	public void setSubGroupNo(int subGroupNo) {
		this.subGroupNo = subGroupNo;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public int getNumberOfDigit() {
		return numberOfDigit;
	}
	public void setNumberOfDigit(int numberOfDigit) {
		this.numberOfDigit = numberOfDigit;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
  
}
