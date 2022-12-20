package com.example.assignment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.Question
import com.example.assignment.R

class QuestionAdapter( val triviaList : ArrayList<Question>) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>(){

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val question = view.findViewById<TextView>(R.id.questionResponseText);
        val answer = view.findViewById<TextView>(R.id.answerResponseText);
        val date = view.findViewById<TextView>(R.id.dateResponseText);
        val valuee = view.findViewById<TextView>(R.id.valueResponseText);
        val category = view.findViewById<TextView>(R.id.categoryResponseText);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context;
        val inflater = LayoutInflater.from(context);
        val questionCardElement = inflater.inflate(R.layout.question_list_element,parent,false);

        return ViewHolder(questionCardElement);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentQuestion : Question = triviaList[position];
        //bind the view with the model
        val question =  holder.question;
        val date = holder.date
        val answer = holder.answer
        val value = holder.valuee
        val category = holder.category

        question.setText(currentQuestion.question)
        date.setText(currentQuestion.date)
        answer.setText(currentQuestion.answer)
        value.setText(currentQuestion.value.toString())
        category.setText(currentQuestion.category)
        println("Salut")
    }

    override fun getItemCount(): Int {
       return triviaList.size;
    }
}