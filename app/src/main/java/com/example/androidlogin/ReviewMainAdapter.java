package com.example.androidlogin;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import listener.OnPostListener;


//Review페이지의 처음 창을 보여주는 MainAdapter
public class ReviewMainAdapter extends RecyclerView.Adapter<ReviewMainAdapter.MainViewHolder>{
    private ArrayList<ReviewPostInfo> mDataset;
    private Activity activity;
    private OnPostListener onPostListener;
    private String email;
    TextView textEmail;
    CardView cardView1;


    //RecyclerView와 cardView를 이용하여 등록된 리뷰를 전체 리스트로 출력할것임. 그것을 위한 정의
    static class MainViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        MainViewHolder(Activity activity, CardView v, ReviewPostInfo reviewPostInfo) {
            super(v);
            cardView = v;
        }
    }//게시물 업데이트해주는 listener를 초기화해줌
    public void setOnPostListener(OnPostListener onPostListener){//인터페이스 초기화해줌
        this.onPostListener = onPostListener;
    }

    //viewType이 계속 0만 주기 때문에 사용하려면 override해야함.
    @Override
    public int getItemViewType(int position){
        return position;
    }

    //배열로 들어온 데이터들을 불러오는 작업.
    ReviewMainAdapter(Activity activity, ArrayList<ReviewPostInfo> mDataset) {//생성자. 초기화해줌
        this.mDataset = mDataset;
        this.activity = activity;
    }

    @NonNull
    @Override//RecyclerView와 cardView를 만들어주는 작업. 보이는 부분만 load함.
    public ReviewMainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout을 view객체로 만들기 위해 layoutInflater를 이용한다.
        final CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_post, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(activity, cardView, mDataset.get(viewType));//cardview가 하나하나 돌때, position값을 알기위해 viewType을 넣어 만듬.
        //Log.e("로그: ","로그: "+viewType);

        cardView.setOnClickListener(new View.OnClickListener() {//하나의 카드뷰를 클릭 시 intent로 해당하는 값을 ReviewActivityPost로넘겨줌.
            @Override
            public void onClick(View view) {
                //postInfo 데이터를 보내줘야 데이터를 가지고 레이아웃에 그려줌.
                Intent intent = new Intent(activity, ReviewActivityPost.class);
                intent.putExtra("postInfo", mDataset.get(mainViewHolder.getAdapterPosition()));//앞에는 key값, 뒤에는 실제 값
                //postInfo의 이름으로 intent를 보내 PostActivity에서 받아서 쓸수있게함
                activity.startActivity(intent);
            }
        });

        //수정,삭제의 popup메뉴를 보여주는 버튼을 cardview로 정의함.
        // 버튼을 클릭시 popup메뉴를 보여주는 코드임.

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //현재 로그인중인 유저


        if(user != null)  {
            email = user.getEmail();
            cardView1= cardView.findViewById(R.id.menu);
            if (email.equals(mDataset.get(viewType).getEmail())) {
                cardView1.setVisibility(View.VISIBLE);
            } else {
                cardView1.setVisibility(View.GONE);
            }
        }
        else {
            Log.e("error : ", "error");
        }



        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, mainViewHolder.getAdapterPosition());
            }
        });
        return mainViewHolder;
    }

    //여기서 dataset 작엽을 해주는것이 좋음.
    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        //item_post에 실제 db들의 값들을 넣어주는 작업.

        //CardView에 title값 넣어주기
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titleTextView);
        titleTextView.setText(mDataset.get(position).getTitle());


        //게시물을 추가한 날짜 넣어주기
        TextView createTextView = cardView.findViewById(R.id.createdTextView);
        createTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));

        //contents값 넣어주기
        final TextView contentsTextView = cardView.findViewById(R.id.contentsTextView);
        contentsTextView.setText(mDataset.get(position).getContents());


        textEmail = cardView.findViewById(R.id.textView2);
        textEmail.setText(mDataset.get(position).getEmail());


    }

    @Override //자동 override됨. 데이터들의 수를 세줌.
    public int getItemCount() {
        return (mDataset != null ? mDataset.size() : 0);
    }

    //popup메뉴를 만들기 위한 메서드. view로 받아오기 위해 activity사용함. 여기서 popup메뉴는 수정 삭제가 내려오는 메뉴임.
    public void showPopup(View v, final int position) {//android studio에서 제공하는 팝업 메뉴 표시 기능
        //db값을 갖고오고, 선택된 post값을 알아오기 위해 사용함. view와 위치값(position)을 갖고와서 사용하기. 하나의 postID를 알아야함.
        //postID를 알아야 그 post를 삭제할수있음.->postInfo.java수정

        //TextView textEmailView = cardView.findViewById(R.id.textView2);


        //수정,삭제의 popup메뉴를 보여주는 버튼을 cardview로 정의함.
        // 버튼을 클릭시 popup메뉴를 보여주는 코드임.


        PopupMenu popup = new PopupMenu(activity,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override//popup메뉴 내의 삭제버튼, 수정버튼을 눌렀을 때 삭제,수정기능 구현
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.modify ://modify버튼을 눌렀을 때
                        onPostListener.onModify(position);//인터페이스의 onModify를 이용
                        return true;
                    case R.id.delete://delete버튼을 눌렀을 때
                        // 게시글 삭제를 위한 메서드. db에서도 삭제함.
                        onPostListener.onDelete(position);//인터페이스의 onDelete를 이용
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();//inflater를 이용하여 view화 시킴
        inflater.inflate(R.menu.post, popup.getMenu());//popup메뉴를 보여줌.
        popup.show();
    }
}