package hk.com.chengwailim.basecomponents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.beardedhen.androidbootstrap.BootstrapButton
import hk.com.chengwailim.basecomponents.Util.BaseDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BaseDialog.confirmDialog(this, "Failed", "message", {})
    }
}
