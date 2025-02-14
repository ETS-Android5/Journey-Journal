package ws.billy.app.Code.Navigation.Navigation;

import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ws.billy.app.R;

// I ended up not using this as I didn't have enough time.

public class LoggedinNavigation extends AppCompatActivity implements View.OnClickListener{
    SlidingPaneLayout mSlidingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);

        mSlidingPanel = (SlidingPaneLayout) findViewById(R.id.SlidingPanel);
        mSlidingPanel.setPanelSlideListener(panelListener);
        mSlidingPanel.setParallaxDistance(100);
        mSlidingPanel.setSliderFadeColor(ContextCompat.getColor(this, android.R.color.transparent));
//        mSlidingPanel.setShadowResource(R.drawable.slidingpaneshadow);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle("Menu");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_camera);
        }
    }

    SlidingPaneLayout.PanelSlideListener panelListener = new SlidingPaneLayout.PanelSlideListener(){

        @Override
        public void onPanelClosed(View arg0) {
            // TODO Auto-genxxerated method stub

        }

        @Override
        public void onPanelOpened(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPanelSlide(View arg0, float arg1) {
            // TODO Auto-generated method stub

        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mSlidingPanel.isOpen()){
                    mSlidingPanel.closePane();
                }else{
                    mSlidingPanel.openPane();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                setButtonSelected(view);
                Toast.makeText(this, "Button profile clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                setButtonSelected(view);
                Toast.makeText(this, "Button notification clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                setButtonSelected(view);
                Toast.makeText(this, "Button settings clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button4:
                setButtonSelected(view);
                Toast.makeText(this, "Button feed clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button5:
                setButtonSelected(view);
                Toast.makeText(this, "Button photos clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button6:
                setButtonSelected(view);
                Toast.makeText(this, "Button Videos clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button7:
                setButtonSelected(view);
                Toast.makeText(this, "Button logout clicked!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void setButtonSelected(final View v){
        mSlidingPanel.closePane();
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.menuContainer);
        for(int index = 0; index<(viewGroup).getChildCount(); ++index) {
            View nextChild = (viewGroup).getChildAt(index);
            nextChild.setBackground(ContextCompat.getDrawable(LoggedinNavigation.this, R.drawable.menunavigation2_buttonmenu));
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                v.setBackgroundColor(ContextCompat.getColor(LoggedinNavigation.this, android.R.color.white));
            }
        }, 200);
    }
}
