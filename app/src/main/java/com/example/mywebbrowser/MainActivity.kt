package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email
import org.jetbrains.anko.sendSMS
import org.jetbrains.anko.share

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 웹뷰 기본 설정
        webView.apply{
            settings.javaScriptEnabled=true     // 자바 스크립트 기능 동작
            webViewClient= WebViewClient()      // 클래스 지정. 이거 안하면 웹뷰에 페이지가 표시되지 않고 자체 웹 브라우저가 동작함.
        }

        webView.loadUrl("http://www.google.com") // 웹뷰에 페이지 로딩

        // 키보드 검색 버튼
        urlEditText.setOnEditorActionListener{_, actionId, _ ->  // editText가 선택되고 글자가 입력될 때마다 호출됨. 인자: 반응한 뷰, 액션ID, 이벤트(사용하지 않는 것은 _로)
            if(actionId==EditorInfo.IME_ACTION_SEARCH){          // EditorInfo 클래스에 상수로 정의된 값 중에서 검색 버튼에 해당하는 상수와 비교하여 검색 버튼이 눌렸는지 확인
                webView.loadUrl(urlEditText.text.toString())     // 검색 창에 입력한 주소를 웹뷰에 전달하여 로딩
                true
            } else{
                false
            }
        }

        // 컨텍스트 메뉴를 표시할 뷰
        registerForContextMenu(webView)
    }

    // 뒤로가기
    override fun onBackPressed() {
        if(webView.canGoBack()){    // 웹뷰가 이전페이지로 갈 수 있다면
            webView.goBack()        // 이전 페이지로 이동
        } else{
            super.onBackPressed()   // 그렇지 않다면 원래 동작 수행(액티비티 종료)
        }
    }

    // 옵션 메뉴를 액티비티에 표시
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)     // 메뉴 리소스를 액티비티 메뉴로 지정
        return true                               // 액티비티에 메뉴가 있다고 인식
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_google, R.id.action_home -> {   // 구글, 집 아이콘 클릭 시 구글 페이지 로딩
                webView.loadUrl("http://www.google.com")
                return true
            }
            R.id.action_naver -> {                      // 네이버 클릭 시 네이버 페이지 로딩
                webView.loadUrl("http://www.naver.com")
                return true
            }
            R.id.action_daum -> {                      // 다음 클릭 시 다음 페이지 로딩
                webView.loadUrl("http://www.daum.net")
                return true
            }
            R.id.action_call -> {                      // 연락처 클릭 시 전화 앱 실행됨. 암시적 인텐트 사용
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:031-123-4567")
                if (intent.resolveActivity(packageManager) != null){    // 이 인텐트를 수행하는 액티비티가 있다면
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {                // 문자 보내기
                sendSMS("010-000-1234", webView.url)    // 해당 전화번호로 웹 페이지 주소를 문자로 보냄.(Anko 암시적 인텐트)
                return true
            }
            R.id.action_email -> {                    // 이메일 보내기
                email("aaa@email.com", "사이트이메일", webView.url) // 해당 이메일 주소로 '사이트이메일'이라는 제목의 이메일 보냄.(Anko 암시적 인텐트)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 컨텍스트 메뉴
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_share -> {                  // 페이지 공유
                share(webView.url)                  // 웹 페이지 주소를 문자열을 공유하는 앱을 사용해 공유(Anko 암시적 인텐트)
            }
            R.id.action_browser -> {                // 기본 웹 브라우저에서 열기
                browse(webView.url)                 // 기기에 기본 브라우저로 웹 페이지 주소를 다시 열기(Anko 암시적 인텐트)
            }
        }
        return super.onContextItemSelected(item)
    }
}