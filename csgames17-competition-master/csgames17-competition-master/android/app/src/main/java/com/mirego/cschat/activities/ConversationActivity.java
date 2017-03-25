package com.mirego.cschat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mirego.cschat.IdentityChatApplication;
import com.mirego.cschat.R;
import com.mirego.cschat.adapters.MessageAdapter;
import com.mirego.cschat.controller.ConversationController;
import com.mirego.cschat.viewdatas.ConversationViewData;
import com.mirego.cschat.viewdatas.MessageViewData;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ConversationActivity extends BaseActivity implements MessageAdapter.MessageAdapterListener {

    private static final String EXTRA_CONVERSATION_ID = "extraConversationId";

    @Inject
    ConversationController controller;

    @BindView(R.id.rv_conversation)
    RecyclerView rvConversation;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.conversation_root)
    ViewGroup root;

    @BindView(R.id.edt_message_input)
    EditText edtMessageInput;

    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private MessageAdapter messageAdapter;

    public static Intent intent(Context context, String conversationId) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra(EXTRA_CONVERSATION_ID, conversationId);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ((IdentityChatApplication) getApplication()).component().inject(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.conversation_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureMessagesRecyclerView();
        configureMessageInput();
    }

    private void configureMessageInput(){
        edtMessageInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    onSendMessageClicked();
                    return true;
                }
                return false;
            }
        });
    }

    private void configureMessagesRecyclerView() {
        messageAdapter = new MessageAdapter(this, this);
        rvConversation.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.list_spacing, null));
        rvConversation.addItemDecoration(itemDecoration);
        rvConversation.setAdapter(messageAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getConversationId() != null) {
            fetchConversation();
        } else {
            // todo
            createConversation("ZxPI0nf1mAAp4FSM");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchConversation() {
        pbLoading.setVisibility(View.VISIBLE);
        controller.getConversation(getConversationId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<ConversationViewData>() {
                    @Override
                    public void accept(@NonNull ConversationViewData conversationViewData) throws Exception {
                        if (conversationViewData != null && conversationViewData.messages() != null) {
                            messageAdapter.populateMessages(conversationViewData.messages());
                            scrollToBottom();
                        }
                        if (conversationViewData != null && conversationViewData.conversationTitle() != null) {
                            toolbar.setTitle(conversationViewData.conversationTitle());
                        }
                        pbLoading.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Snackbar.make(root, R.string.network_error, Snackbar.LENGTH_SHORT).show();
                        pbLoading.setVisibility(View.GONE);
                    }
                });
    }


    private void createConversation(String userId) {
        controller.createConversation(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<ConversationViewData>>() {
                    @Override
                    public void accept(@NonNull List<ConversationViewData> conversationViewDatas) throws Exception {
                        // todo
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        // todo
                    }
                });
    }


    @OnClick(R.id.btn_send_message)
    public void onSendMessageClicked() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtMessageInput.getWindowToken(), 0);

        createMessage(edtMessageInput.getText().toString());
        edtMessageInput.setText("");
    }

    private void createMessage(String message) {
        pbLoading.setVisibility(View.VISIBLE);
        controller.createMessage(getConversationId(), message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<ConversationViewData>() {
                    @Override
                    public void accept(@NonNull ConversationViewData conversationViewData) throws Exception {
                        if (conversationViewData != null && conversationViewData.messages() != null) {
                            messageAdapter.populateMessages(conversationViewData.messages());
                            scrollToBottom();
                        }
                        pbLoading.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Snackbar.make(root, R.string.network_error, Snackbar.LENGTH_SHORT).show();
                        pbLoading.setVisibility(View.GONE);
                    }
                });
    }

    private String getConversationId() {
        return getIntent().getStringExtra(EXTRA_CONVERSATION_ID);
    }

    @Override
    public void onMessageClicked(MessageViewData messageViewData) {
        Toast.makeText(this, getString(R.string.not_implemented_yet), Toast.LENGTH_LONG).show();
    }

    private void scrollToBottom(){
        rvConversation.smoothScrollToPosition(messageAdapter.getItemCount());
    }
}
