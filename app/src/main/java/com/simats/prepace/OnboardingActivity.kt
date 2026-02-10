package com.simats.prepace

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.simats.prepace.model.OnboardingItem

class OnboardingActivity : AppCompatActivity() {

    private lateinit var ivOnboarding: ImageView
    private lateinit var flIconContainer: android.widget.FrameLayout
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnNext: androidx.appcompat.widget.AppCompatButton
    private lateinit var tvSkip: TextView
    
    private lateinit var dot1: View
    private lateinit var dot2: View
    private lateinit var dot3: View

    private var currentPage = 0
    private lateinit var onboardingItems: List<OnboardingItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        ivOnboarding = findViewById(R.id.ivOnboarding)
        flIconContainer = findViewById(R.id.flIconContainer)
        tvTitle = findViewById(R.id.tvTitle)
        tvDescription = findViewById(R.id.tvDescription)
        btnNext = findViewById(R.id.btnNext)
        tvSkip = findViewById(R.id.tvSkip)
        
        dot1 = findViewById(R.id.dot1)
        dot2 = findViewById(R.id.dot2)
        dot3 = findViewById(R.id.dot3)

        setupData()
        updateUI()

        btnNext.setOnClickListener {
            if (currentPage < onboardingItems.size - 1) {
                currentPage++
                updateUIWithAnimation()
            } else {
                navigateToCreateAccount()
            }
        }

        tvSkip.setOnClickListener {
            navigateToCreateAccount()
        }
    }

    private fun setupData() {
        onboardingItems = listOf(
            OnboardingItem(
                "Smart Quiz-\nBased Learning",
                "Master concepts through interactive\nquizzes designed to enhance your\nunderstanding",
                R.drawable.ic_book,
                R.drawable.bg_circle_container
            ),
            OnboardingItem(
                "Track Your\nImprovement Easily",
                "Monitor your progress with detailed\nanalytics and performance insights",
                R.drawable.ic_graph,
                R.drawable.bg_circle_purple_solid
            ),
            OnboardingItem(
                "Improve Skills Step\nby Step",
                "Build expertise gradually with structured\nlearning paths and practice",
                R.drawable.ic_target,
                R.drawable.bg_circle_blue_solid
            )
        )
    }

    private fun updateUIWithAnimation() {
        val fadeOut = AlphaAnimation(1.0f, 0.0f)
        fadeOut.duration = 200
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                updateUI()
                val fadeIn = AlphaAnimation(0.0f, 1.0f)
                fadeIn.duration = 200
                findViewById<View>(R.id.content_container).startAnimation(fadeIn)
            }
        })
        findViewById<View>(R.id.content_container).startAnimation(fadeOut)
    }

    private fun updateUI() {
        val item = onboardingItems[currentPage]
        ivOnboarding.setImageResource(item.imageResId)
        flIconContainer.setBackgroundResource(item.bgResId)
        tvTitle.text = item.title
        tvDescription.text = item.description

        if (currentPage == onboardingItems.size - 1) {
            btnNext.text = "Get Started"
            tvSkip.visibility = View.GONE
        } else {
            btnNext.text = "Next"
            tvSkip.visibility = View.VISIBLE
        }

        updateIndicators()
    }

    private fun updateIndicators() {
        val activeWidth = resources.getIdentifier("dimen_24", "dimen", packageName).let { id ->
            if (id != 0) resources.getDimensionPixelSize(id)
            else (24 * resources.displayMetrics.density).toInt()
        }

        val activeParams = android.widget.LinearLayout.LayoutParams(
            activeWidth,
            (8 * resources.displayMetrics.density).toInt()
        ).apply { setMargins((4 * resources.displayMetrics.density).toInt(), 0, (4 * resources.displayMetrics.density).toInt(), 0) }
        
        val inactiveParams = android.widget.LinearLayout.LayoutParams(
            (8 * resources.displayMetrics.density).toInt(),
            (8 * resources.displayMetrics.density).toInt()
        ).apply { setMargins((4 * resources.displayMetrics.density).toInt(), 0, (4 * resources.displayMetrics.density).toInt(), 0) }

        dot1.layoutParams = if (currentPage == 0) activeParams else inactiveParams
        dot2.layoutParams = if (currentPage == 1) activeParams else inactiveParams
        dot3.layoutParams = if (currentPage == 2) activeParams else inactiveParams

        dot1.setBackgroundResource(if (currentPage == 0) R.drawable.dot_active_pill else R.drawable.dot_inactive)
        dot2.setBackgroundResource(if (currentPage == 1) R.drawable.dot_active_pill else R.drawable.dot_inactive)
        dot3.setBackgroundResource(if (currentPage == 2) R.drawable.dot_active_pill else R.drawable.dot_inactive)
    }

    private fun navigateToCreateAccount() {
        startActivity(Intent(this, CreateAccountActivity::class.java))
        finish()
    }
}
