/* 최초 작성 18.11.16
 *
 * 시작버튼을 누르면 1 ~ 360도 사이의 랜덤값을 설정 후 IF문 조건식에 맞는 당첨아이템의 내용을 문자열로 저장
 * 애니메이션 함수를 호출해 랜덤값을회전시작
 * 회전 후 당첨아이템의 내용을 다이얼로그를 이용해 화면에 출력
 *
 * -------------------------------------- 로직흐름 --------------------------------------
 * 1. 변수 선언
 *      1-1. 버튼 변수(애니메이션 시작)
 *      1-2. 이미지뷰 변수(룰렛 이미지)
 *      1-3. 애니메이션 관련 변수(애니메이션 함수를 사용하기전 미리 선언)
 *      1-4. 이미지 회전값을 담을 변수
 *      1-5. 선택된 아이템의 내용을 담을 변수
 *
 * 2. onCreate()함수
 *      2-1. findViewById()함수를 이용해 버튼변수, 이미지뷰변수에 id값이
 *      일치하는 뷰를 찾아 리소스파일에서 찾아 초기화
 *      2-2. SoundPool인스턴스를 생성해 음악재생 관련 설정을 함
 *      2-3. 시작버튼에 클릭 이벤트 리스너 등록
 *           (변수값 초기화, 회전값 설정 및 아이템선정, 회전 애니메이션 시작, 사운드 재생 시작)
 *      2-4. 헨들러, 스레드 이용해서 당첨된 아이템의 메시지를 8초 후 다이얼로그에 띄움
 *      2-5. 회전값 초기화
 *
 * 3. randSpin(float rand)함수
 *      3-1. 매개변수로 넘어온 1 ~ 360사이의 회전랜덤값을 IF문으로 회전영역
 *           일치여부 판단해서 당첨아이템 내용을 문자열변수에 저장
 *     if(0 < rand && rand < 45) {
 *           text = "당첨아이템 내용";
 *     } else if(...) { ... }
 *     위 코드는 예시임.
 *
 * 4. animation()함수
 *      4-1. 기본 10바퀴 + 랜덤1바퀴 설정
 *      4-2. 애니메이션 설정 : 회전, 0 ~ 랜덤값까지
 *      4-3. 애니메이션 효과 : 점점빠르게 -> 보통속도 -> 점점느리게
 *      4-4. 애니메이션 간격 : 8초
 *      4-5.
 *  -------------------------------------------------------------------------------------
 * */

package kim.youngjoon.myapp;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import kim.youngjoon.myapp.R;
import static java.lang.String.valueOf;
import static kim.youngjoon.myapp.R.raw.rouletteplay;

public class MainActivity extends AppCompatActivity {
    Button btn;
//  Button btn2;
    ImageView iv;
    ImageView iv2;
    ObjectAnimator object;
    int rotate = 0;
    int random= 0;
    public static String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        iv = (ImageView) findViewById(R.id.Roulette);
        iv2 = (ImageView) findViewById(R.id.select);

        final SoundPool sp = new SoundPool(1,         // 최대 음악파일의 개수
                AudioManager.STREAM_MUSIC, // 스트림 타입
                0);        // 음질 - 기본값:0
        /* SoundPool 객체 생성 */

        final int soundID = sp.load(this, // 현재 화면의 제어권자
                rouletteplay,    // 음악 파일 경로지정
                1);        // 우선순위
        /* SoundPool.load() 로 사운드 리소스 파일 id 준비 */

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                random = (int)(Math.random() * 360) + 1;
                randSpin(random);
                /* 회전값 설정 및 아이템선정 */

                animation();
                /* 회전 애니메이션 시작 */

                sp.play(soundID, // 준비한 soundID
                        1,         // 왼쪽 볼륨 float 0.0(작은소리)~1.0(큰소리)
                        1,         // 오른쪽볼륨 float
                        0,         // 우선순위 int
                        0,     // 반복회수 int -1:무한반복, 0:반복안함
                        0.5f    // 재생속도 float 0.5(절반속도)~2.0(2배속)
                );
                /* 사운드 재생 시작 */

                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dialog.setTitle("당첨을 축하드립니다^-^").setMessage(text).create().show();
                        /* 화면에 당첨아이템 내용출력 */
                    }
                }, 8010);

                rotate = 0;
                random = 0;
                /* 회전값 초기화 */
            }
        });
    }

    public void randSpin(int rand) {
        text = "";
        if((337.5 < rand && rand <= 360) || rand < 22.5) {
            text = "아메리카노 당첨!!!";
        } else if(22.5 < rand && rand < 67.5) {
            text = "포테이토 피자 당첨!!!";
        } else if(67.5 < rand && rand < 112.5) {
            text = "모바일 상품권 당첨!!!";
        } else if(112.5 < rand && rand < 157.5) {
            text = "미디어팩 당첨!!!";
        } else if(157.5 < rand && rand < 202.5) {
            text = "미니케익 당첨!!!";
        } else if(202.5 < rand && rand < 247.5) {
            text = "올레TV 당첨!!!";
        } else if(247.5 < rand && rand < 292.5) {
            text = "더블레귤러 당첨!!!";
        } else if(292.5 < rand && rand < 337.5) {
            text = "데이터충전 당첨!!!";
        }
    }

    void animation() {
        rotate = 3600 + random;
        /* 기본 10바퀴 + 랜덤1바퀴 */
        object = ObjectAnimator.ofFloat(iv, "rotation", 0, rotate);
        /* 애니메이션 설정 : 회전, 0 ~ 랜덤값까지 */
        object.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.accelerate_decelerate_interpolator));
        /* 애니메이션 효과 : 점점빠르게 -> 보통속도 -> 점점느리게 */
        object.setDuration(8000);
        /* 애니메이션 간격 : 8초 */
        object.start();
        /* 애니메이션 회전시작 */
    }
}