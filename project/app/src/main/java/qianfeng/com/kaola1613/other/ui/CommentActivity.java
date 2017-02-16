package qianfeng.com.kaola1613.other.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.entity.Recommend2;
import qianfeng.com.kaola1613.other.adapter.CommentListAdapter;
import qianfeng.com.kaola1613.other.entity.Comment;
import qianfeng.com.kaola1613.other.utils.Contants;
import qianfeng.com.kaola1613.other.utils.HttpUtil;
import qianfeng.com.kaola1613.other.utils.KaolaTask;
import qianfeng.com.kaola1613.other.utils.OtherHttpUtil;

/**
 * 评论页面
 *
 */
public class CommentActivity extends AppCompatActivity {

    private ListView listView;

    private KaolaTask listTask;

    private long resourceid;

    private EditText editText;

    private KaolaTask commentTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        editText = (EditText) findViewById(R.id.comment_content_et);

        Recommend2 recommend2 = getIntent().getParcelableExtra(Player1Activity.TAG_RECOMMED2);
        resourceid = recommend2.getRid();

        listView = (ListView) findViewById(R.id.comment_lv);
    }

    public void submitComment(View view)
    {
        commentTask = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {

                Map<String, String> params = new HashMap<>();
                params.put("revieweruid", "2754846");
                params.put("resourcetype", "1");
                params.put("reviewername", "QianFengLaoLiu");
                params.put("commenttype", "0");
                params.put("resourceid", ""+resourceid);
                params.put("content", editText.getText().toString().trim());

                return HttpUtil.doPost(OtherHttpUtil.URL_SUBMIT_COMMENT, params);
            }

            @Override
            public Object parseResult(Object obj) {
                try {
                    JSONObject root = new JSONObject(obj.toString());

                    String code = root.getString(Contants.JSON_FLAG_CODE);
                    String message = root.getString(Contants.JSON_FLAG_MESSAGE);

                    if (Contants.JSON_FLAG_CODE_SUCCESS.equals(code)
                            && Contants.JSON_FLAG_MESSAGE_SUCCESS.equals(message))
                    {
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return obj;
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {

                boolean result = (boolean) object;
                //如果请求成功了，那么重新请求列表
                if (result)
                {
                    editText.setText("");
                    Toast.makeText(CommentActivity.this, "评论成功!", Toast.LENGTH_SHORT).show();
                    showList();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(CommentActivity.this, "评论失败!", Toast.LENGTH_SHORT).show();
            }
        });

        commentTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showList();
    }

    private void showList() {
        listTask = new KaolaTask(new KaolaTask.IRequest() {
            @Override
            public Object doRequest() {
                return HttpUtil.doGet(OtherHttpUtil.URL_COMMENT_LIST + resourceid);
            }

            @Override
            public Object parseResult(Object obj) {
                try {
                    JSONObject root = new JSONObject(obj.toString());

                    String message = root.getString(Contants.JSON_FLAG_MESSAGE);

                    String code = root.getString(Contants.JSON_FLAG_CODE);

                    if (Contants.JSON_FLAG_CODE_SUCCESS.equals(code)
                            && Contants.JSON_FLAG_MESSAGE_SUCCESS.equals(message))
                    {
                        JSONObject result = root.getJSONObject(Contants.JSON_FLAG_RESULT);

                        List<Comment> commentList = Comment.arrayCommentFromData(result.toString(), Contants.JSON_FLAG_DATALIST);
                        return commentList;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                List<Comment> list = (List<Comment>) object;
                CommentListAdapter adapter = new CommentListAdapter(CommentActivity.this, list);
                listView.setAdapter(adapter);

                recycleTask();
            }

            @Override
            public void onError() {
                Toast.makeText(CommentActivity.this, "请求列表失败!", Toast.LENGTH_SHORT).show();
                recycleTask();
            }
        });

        listTask.execute();
    }

    private void recycleTask() {
        listTask.cancel(false);
        listTask = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (listTask != null)
        {
            listTask.cancel(true);
            listTask = null;
        }

        if (commentTask != null)
        {
            commentTask.cancel(true);
            commentTask = null;
        }

    }
}
