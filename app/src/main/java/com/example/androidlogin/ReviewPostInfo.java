package com.example.androidlogin;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//getter와 setter를 정의해주는 코드
public class ReviewPostInfo implements Serializable {
    //intent에서 putExtra로 보내주기 위해 implements Serializable가 사용됨.
    private String title;
    private String contents;
    private Date createdAt;
    private String email;
    private String id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //id값을 불러올 때 사용되는 생성자함수
    public ReviewPostInfo(String title, String Contents, Date createdAt, String id,String email) {
        this.title = title;
        this.contents = Contents;
        this.createdAt = createdAt;
        this.id = id;
        this.email = email;
    }


    //게시물을 등록할때 id값이 필요없기 때문에 id값이 없는 생성자가 필요함함
   public ReviewPostInfo(String title, String Contents, Date createdAt, String email) {
        this.title = title;
        this.contents = Contents;
        this.createdAt = createdAt;
        this.email = email;
    }


    //데이터베이스에 불필요한 id필드값이 들어가기 때문에 전용getter를 만들어줌.
    // setter를 정의하면 그냥 다 들어감. 그래서 getter를 별도로 만들어주었음.
    public Map<String,Object> getPostInfo(){
        Map<String,Object> docData = new HashMap<>();
        docData.put("title",title);
        docData.put("contents",contents);
        docData.put("createdAt",createdAt);
        docData.put("user",email);
        return docData;
    }
    //get set 메서드 이용
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
