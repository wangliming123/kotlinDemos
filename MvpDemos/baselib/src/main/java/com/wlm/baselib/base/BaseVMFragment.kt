package com.wlm.baselib.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wlm.baselib.utils.ToastUtils
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseVMFragment<VM : BaseViewModel> : Fragment(), EasyPermissions.PermissionCallbacks {

    lateinit var mViewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVM()
        startObserve()
        initView()
        initData()
    }

    private fun initVM() {
        mViewModel = ViewModelProviders.of(this).get(providerVMClass())
        lifecycle.addObserver(mViewModel)
    }

    override fun onDestroy() {
        lifecycle.removeObserver(mViewModel)
        super.onDestroy()
    }

    open fun startObserve() {
        mViewModel.mException.observe(this, Observer { onError(it) })
    }

    /**
     * Activity对应ViewModel类
     */
    abstract fun providerVMClass(): Class<VM>

    /**
     * 布局
     */
    abstract fun layoutId(): Int

    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 初始化数据
     */
    abstract fun initData()

    open fun onError(e: Throwable){
        Log.d("onError", "${providerVMClass().name} onError: ${e.message}")
    }

    /**
     * 重写要申请权限的Activity或者Fragment的onRequestPermissionsResult()方法，
     * 在里面调用EasyPermissions.onRequestPermissionsResult()，实现回调。
     *
     * @param requestCode  权限请求的识别码
     * @param permissions  申请的权限
     * @param grantResults 授权结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 当权限被成功申请的时候执行回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.i("EasyPermission", "成功申请权限$perms")
    }

    /**
     * 当权限申请失败的时候执行的回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        val sb = StringBuffer()
        for (str in perms) {
            sb.append(str)
            sb.append("\n")
        }
        sb.replace(sb.length - 2, sb.length, "")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            ToastUtils.showShort(context!!, "已拒绝权限${sb}并不在询问")
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要${sb}权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("是")
                .setNegativeButton("否")
                .build()
                .show()
        }
    }

}