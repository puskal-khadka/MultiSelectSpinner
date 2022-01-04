package com.puskal.multiselectspinner

import android.content.Context
import android.graphics.Typeface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat

/**created by Puskal khadka
 * 12 may, 2021
 */
class MultipleSelectSpinnerAdapter(
    val myContext: Context,
    val resource: Int,
    val dataList: ArrayList<MultipleSelectSpinnerPojo>,
    val showSelectTitleHeading: Boolean = true,
    val isShowSelect: Boolean = true,
    var placeholderText:String,
    var confirmTextColor: Int,
    var checkedListener: (ArrayList<MultipleSelectSpinnerPojo>) -> Unit
) : ArrayAdapter<MultipleSelectSpinnerPojo>(myContext, resource, dataList) {
    var tempList= arrayListOf<MultipleSelectSpinnerPojo>()

    init {
        tempList=dataList.map { it.copy(it.text,it.isSelected) }.toCollection(arrayListOf())
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return customOuterView(position, convertView, parent)
    }

    fun customOuterView(position: Int, convertView: View?, parent: ViewGroup): View {
        var viewHolder: VH? = null
        var returnView: View? = convertView
        if (convertView == null) {
            returnView = LayoutInflater.from(myContext)
                .inflate(android.R.layout.simple_spinner_dropdown_item, null)
            val text = returnView.findViewById<CheckedTextView>(android.R.id.text1)
            text.text = placeholderText
        } else {
            viewHolder = convertView.getTag() as VH?
        }


        return returnView!!


    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return customDropdownView(position, convertView, parent)
    }

    fun customDropdownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var viewHolder: VH? = null

        var returnView: View? = convertView
        if (convertView == null) {
            returnView =
                LayoutInflater.from(myContext).inflate(R.layout.spinner_checkbox_dropdown, null)
            viewHolder = VH(returnView)
            returnView.setTag(viewHolder)

        } else {
            viewHolder = convertView.getTag() as VH?
        }

        if (position == 0 && showSelectTitleHeading) {
            if (dataList.size == 2) {
                viewHolder!!.rlItem.visibility=View.GONE
            }
        }

        if (position == dataList.size - 1) {
            viewHolder!!.isSelected.visibility = View.GONE
            viewHolder.myText.setTextColor(confirmTextColor)
            viewHolder.myText.apply {
                setPadding(0, 30, 0, 30)
                textSize = 14F
                setTypeface(null, Typeface.BOLD)
            }
        }
        viewHolder!!.myText.text = dataList[position].text
        viewHolder.isSelected.isChecked = dataList[position].isSelected



        viewHolder.isSelected.setOnClickListener {
            //for making no change when dropdown cancel without touching cancel
            val isChecked=(it as CompoundButton).isChecked
            if (position == 0) {
                for (item in dataList) {
                    item.isSelected = isChecked
                }
            }
            else{
                dataList[0].isSelected=false
                dataList[position].isSelected = isChecked
            }
            checkedListener(dataList)
            notifyDataSetChanged()

        }

        viewHolder.rlItem.setOnClickListener {
            if (position == dataList.size - 1) {
                checkedListener(dataList)

                /**docs->Puskal -> for closing dropdown
                 * WorkAround
                 */
                val root = parent.rootView
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
            }
        }


        return returnView!!


    }


    inner class VH(val view: View) {
        var myText: TextView = view.findViewById(R.id.tvText)
        var isSelected: CheckBox = view.findViewById(R.id.cbCheck)
        var rlItem: RelativeLayout = view.findViewById(R.id.rlItem)

    }
}

