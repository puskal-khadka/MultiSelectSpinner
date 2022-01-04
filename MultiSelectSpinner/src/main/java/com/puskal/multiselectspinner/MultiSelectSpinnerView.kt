package com.puskal.multiselectspinner

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.RelativeLayout

/**@author Puskal khadka
 */
class MultiSelectSpinnerView(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatSpinner(context, attrs) {

    private var headerSelectAll_text: String = "All"
    private var footerConfirm_text: String = "OK"
    private var placeholder_text: String = "Select Option"
    private var confirmTextColor: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiSelectSpinnerView)
        headerSelectAll_text =
            typedArray.getString(R.styleable.MultiSelectSpinnerView_dropdown_headerSelectAll_text)
                ?: "All"
        footerConfirm_text =
            typedArray.getString(R.styleable.MultiSelectSpinnerView_dropdown_footerConfirm_text)
                ?: "OK"
        placeholder_text =
            typedArray.getString(R.styleable.MultiSelectSpinnerView_placeholder_text)
                ?: "Select Option"
        confirmTextColor = typedArray.getColor(
            R.styleable.MultiSelectSpinnerView_confirmTextColor,
            Color.GREEN
        )
        typedArray.recycle()

    }

    /**
     * @param dataList:List of String that you want to show in spinnerDropdown
     * @param selectedSectionListener: callback that you return list of position which are selected and display item string
     */
    fun buildCheckedSpinner(
        dataList: ArrayList<String>,
        selectedSectionListener: (ArrayList<Int>, String) -> Unit
    ) {
        val optionList = arrayListOf(headerSelectAll_text)
        optionList.addAll(dataList)
        if (dataList.isNotEmpty()) {
            optionList.add(footerConfirm_text)
        }

        val spinnerModelList = arrayListOf<MultipleSelectSpinnerPojo>()
        for (item in optionList) {
            spinnerModelList.add(MultipleSelectSpinnerPojo(item, false))
        }


        val spinnerAdapter = MultipleSelectSpinnerAdapter(
            this.context,
            0,
            spinnerModelList,
            showSelectTitleHeading = true,
            true,
            placeholderText = placeholder_text,
            confirmTextColor
        ) { it ->
            var displayText = ""
            var returnSectionColl = ""

            var selectedPosition = arrayListOf<Int>()
            for (i in 0 until it.size) {
                if (i == 0 || i == it.size - 1) {
                    continue
                }
                if (it[i].isSelected) {
                    val item = optionList[i]
                    returnSectionColl += "$item, "
                    displayText += " $item, "
                    selectedPosition.add(i - 1)
                }

            }
            displayText = displayText.replace(", $".toRegex(), "")
            returnSectionColl = returnSectionColl.replace(", $".toRegex(), "")


            if (returnSectionColl.isEmpty()) {
                selectedSectionListener(selectedPosition, "")
            } else {
                selectedSectionListener(selectedPosition, returnSectionColl)
            }

            val rcvDataList = arrayListOf<MultipleSelectSpinnerPojo>()
            rcvDataList.addAll(it)
            spinnerModelList.clear()
            spinnerModelList.addAll(rcvDataList)

            if (this.selectedItemPosition % 2 == 0) {
                this.setSelection(1)
            } else {
                this.setSelection(0)
            }
            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var dispText = if (displayText.isEmpty()) placeholder_text else displayText
                    val text = view?.findViewById<CheckedTextView>(android.R.id.text1)
                    text?.setText(dispText)
                    text?.gravity = Gravity.START
                    val lparam = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    lparam.setMargins(18, 0, 0, 0)
                    text?.layoutParams = lparam
                    val cb = view?.findViewById<CheckBox>(R.id.cbCheck)
                    cb?.visibility = View.GONE
                    displayText = ""
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }
        this.adapter = spinnerAdapter
    }
}