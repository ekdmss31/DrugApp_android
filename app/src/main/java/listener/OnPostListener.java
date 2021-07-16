package listener;

//게시물 업데이트를 위해 interface를 이용함. 삭제와 수정기능을 선언해줌
public interface OnPostListener {
    void onDelete(int position);
    void onModify(int position);
}
