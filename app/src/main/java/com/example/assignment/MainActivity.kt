package com.example.assignment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.assignment.adapter.QuestionAdapter
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() ,View.OnClickListener {

    val list : ArrayList<Question>  = ArrayList();
    var questionAdapter = QuestionAdapter(list);



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val descarcaButton = findViewById<Button>(R.id.descarcaButton);

        val recyclerView = findViewById<RecyclerView>(R.id.recycler)

        recyclerView.layoutManager = LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.adapter = questionAdapter;
        val progressBar = findViewById<ImageView>(R.id.progressBar);
        progressBar.visibility = View.INVISIBLE
        descarcaButton.setOnClickListener(this);

    }

    override fun onClick(v: View) {


        val numarIntrebare = findViewById<EditText>(R.id.nrIntrebariEditText);
        val eroareTextView = findViewById<TextView>(R.id.eroareNumarIntrebareTextView);
        eroareTextView.text="";
        try{
            val numarIntrebareIntrodus = Integer.parseInt(numarIntrebare.text.toString());
            if( numarIntrebareIntrodus > 100 || numarIntrebareIntrodus < 1)
            {
                eroareTextView.text = "Ai introdus un numar gresit. Te rugam sa introduci un numar intre 1 si 100";
            }
            else
            {
                this.list.removeAll(list)
                questionAdapter.notifyDataSetChanged();
                val progressBar = findViewById<ImageView>(R.id.progressBar)
                progressBar.visibility = View.VISIBLE
                var progressTimer= Timer("Download",false).schedule(1000){
                    android.os.Handler(mainLooper).post{
                        getData(numarIntrebareIntrodus)
                        progressBar.visibility = View.INVISIBLE
                        eroareTextView.text = ""
                        numarIntrebare.setText("");
                    }
                }
            }
        }catch (ex: NumberFormatException )
        {
            eroareTextView.text = "Te rog sa introduci un numar";
        }
    }

    fun getData(numarIntrebareIntrodus : Int)
    {
        val queue = Volley.newRequestQueue(this)
        val url = "https://jservice.io/api/random?count=$numarIntrebareIntrodus";

        val jsonArrayRequest = JsonArrayRequest( Request.Method.GET, url, null,
            { response ->

                println("Hello!!!!" + response.length())

                for( i in 0 until response.length())
                {
                    val currentQuestion = response.getJSONObject(i);
                    val qValue = currentQuestion.getString("question")
                    val aValue = currentQuestion.getString("answer");
                    val vString = currentQuestion.getString("value");
                    var vValue = -1;
                    if(vString != "null"){
                        vValue = Integer.parseInt(vString);
                    }
                    val dValue = currentQuestion.getString("created_at");
                    val category = currentQuestion.getJSONObject("category");
                    val cValue = category.getString("title");
                    this.list.add(Question(qValue,aValue, vValue,dValue,cValue))
                }
                println(this.list.size)

            },

            { error -> println(error) })
        questionAdapter.notifyDataSetChanged()
        queue.add(jsonArrayRequest)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate ( R.menu.menu, menu )
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected (item: MenuItem): Boolean {
        val id = item.itemId
        if ( id == R.id.add_question)
        {
            val intent = Intent ( this, AddQuestionActivity::class.java )
            addToListLauncher.launch(intent);
            return true
        }
        return super.onOptionsItemSelected(item);
        }
    @RequiresApi(Build.VERSION_CODES.O)
    var addToListLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        result ->
        if(result.resultCode == RESULT_OK)
        {
            val intent = result.data
            val q = intent?.getStringExtra("question")
            val a = intent?.getStringExtra("answer")
            val v =intent?.getIntExtra("value",50)
            val c  = intent?.getStringExtra("category")
            if(q!=null && a!=null && v!=null && c!=null)
            {
                val question = Question( q,a,v, LocalDateTime.now().toString(),c)
                list.add(0,question)
                questionAdapter.notifyItemInserted(0)
            }
            Toast.makeText(this,"Question was saved",Toast.LENGTH_LONG).show();
        }
    }
}