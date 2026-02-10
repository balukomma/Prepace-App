package com.simats.prepace.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.simats.prepace.R;
import com.simats.prepace.model.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<Notification> notificationList;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.tvTitle.setText(notification.getTitle());
        holder.tvMessage.setText(notification.getMessage());
        holder.tvTime.setText(notification.getTimestamp());

        if (notification.isUnread()) {
            holder.viewUnreadDot.setVisibility(View.VISIBLE);
        } else {
            holder.viewUnreadDot.setVisibility(View.GONE);
        }

        // Set icons and colors based on type
        switch (notification.getType()) {
            case QUIZ_COMPLETED:
                holder.ivIcon.setImageResource(R.drawable.ic_check_circle_white);
                holder.iconContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.brand_blue)));
                break;
            case ACHIEVEMENT_UNLOCKED:
                holder.ivIcon.setImageResource(R.drawable.ic_badge); 
                holder.iconContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.brand_purple)));
                break;
            case DAILY_CHALLENGE:
                holder.ivIcon.setImageResource(R.drawable.ic_clock);
                holder.iconContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.brand_blue))); 
                // Wait, daily challenge in image is purple/blue gradient or solid blue?
                // In image: Quiz Completed -> Blue check.
                // Achievement -> Purple ribbon.
                // Daily Challenge -> Blue Clock.
                // New Quizzes -> Blue Bell.
                // Personal Best -> Blue Trophy.
                // Actually the clock icon background looks like brand_purple or brand_blue. Let's stick to brand_blue for now as per image.
                holder.iconContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.brand_purple))); // Looks purplish blue
                break;
            case NEW_QUIZZES:
                holder.ivIcon.setImageResource(R.drawable.ic_bell);
                holder.iconContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.brand_blue)));
                break;
            case PERSONAL_BEST:
                holder.ivIcon.setImageResource(R.drawable.ic_trophy); // Assuming ic_trophy exists
                holder.iconContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.brand_blue)));
                break;
            default:
                 // Default handling
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMessage, tvTime;
        View viewUnreadDot;
        ImageView ivIcon;
        FrameLayout iconContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            viewUnreadDot = itemView.findViewById(R.id.viewUnreadDot);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            iconContainer = itemView.findViewById(R.id.iconContainer);
        }
    }
}
