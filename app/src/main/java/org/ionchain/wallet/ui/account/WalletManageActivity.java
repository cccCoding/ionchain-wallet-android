package org.ionchain.wallet.ui.account;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fast.lib.logger.Logger;
import com.fast.lib.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.ionchain.wallet.R;
import org.ionchain.wallet.adpter.WalletManageAdapter;
import org.ionchain.wallet.comm.api.model.Wallet;
import org.ionchain.wallet.comm.api.resphonse.ResponseModel;
import org.ionchain.wallet.comm.constants.Comm;
import org.ionchain.wallet.db.WalletDaoTools;
import org.ionchain.wallet.ui.comm.BaseActivity;
import org.ionchain.wallet.ui.wallet.CreateWalletActivity;
import org.ionchain.wallet.ui.wallet.ImprotWalletActivity;
import org.ionchain.wallet.ui.wallet.ModifyWalletActivity;

import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;

public class WalletManageActivity extends BaseActivity implements BGAOnRVItemClickListener,OnRefreshLoadmoreListener {

    @BindView(R.id.srl)
    SmartRefreshLayout srl;

    @BindView(R.id.dataRv)
    RecyclerView dataRv;

    private WalletManageAdapter walletManageAdapter;

    @Override
    public void handleMessage(int what, Object obj) {
        super.handleMessage(what, obj);
        try {
            switch (what) {
                case R.id.navigationBack:
                    finish();
                    break;
                case R.id.createBtn:
                    transfer(CreateWalletActivity.class,Comm.JUMP_PARM_ISADDMODE,true);
                    break;
                case R.id.importBtn:
                    transfer(ImprotWalletActivity.class,Comm.JUMP_PARM_ISADDMODE,true);

                    break;
                case 0:
                    dismissProgressDialog();
                    if (obj == null)
                        return;

                    ResponseModel<String> responseModel = (ResponseModel) obj;
                    if (!verifyStatus(responseModel)) {
                        ToastUtil.showShortToast(responseModel.getMsg());
                        return;
                    }


                    break;
            }

        } catch (Throwable e) {
            Logger.e(e, TAG);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_manage);
        dataRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


    }
    @Override
    protected void setListener() {
        setOnClickListener(R.id.createBtn);
        setOnClickListener(R.id.importBtn);
        walletManageAdapter.setOnRVItemClickListener(this);
        srl.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        walletManageAdapter = new WalletManageAdapter(dataRv);
        dataRv.setAdapter(walletManageAdapter);

        List<Wallet> walletlist =  WalletDaoTools.getAllWallet();

        walletManageAdapter.setData(walletlist);
    }

    @Override
    public int getActivityMenuRes() {
        return 0;
    }

    @Override
    public int getHomeAsUpIndicatorIcon() {
        return R.mipmap.ic_arrow_back;
    }

    @Override
    public int getActivityTitleContent() {
        return R.string.activity_wallet_manage;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        transfer(ModifyWalletActivity.class);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        srl.finishLoadmore();

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        srl.finishRefresh();
        List<Wallet> walletlist =  WalletDaoTools.getAllWallet();

        walletManageAdapter.setData(walletlist);
    }
}
