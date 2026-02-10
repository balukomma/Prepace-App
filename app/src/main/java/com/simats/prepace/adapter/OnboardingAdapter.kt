package com.simats.prepace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.simats.prepace.R
import com.simats.prepace.model.OnboardingItem

class OnboardingAdapter(private val onboardingItems: List<OnboardingItem>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageOnboarding: ImageView = view.findViewById(R.id.ivOnboarding)
        val flIconContainer: android.widget.FrameLayout = view.findViewById(R.id.flIconContainer)
        val textTitle: TextView = view.findViewById(R.id.tvTitle)
        val textDescription: TextView = view.findViewById(R.id.tvDescription)

        fun bind(onboardingItem: OnboardingItem) {
            textTitle.text = onboardingItem.title
            textDescription.text = onboardingItem.description
            imageOnboarding.setImageResource(onboardingItem.imageResId)
            flIconContainer.setBackgroundResource(onboardingItem.bgResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_onboarding,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(onboardingItems[position])
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }
}
