package com.example.finalprojectappraisal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectappraisal.R;
import com.example.finalprojectappraisal.model.Image;
import com.example.finalprojectappraisal.classifer.ImageCategorySection;

import java.util.ArrayList;
import java.util.List;

public class ImageCategoriesAdapter extends RecyclerView.Adapter<ImageCategoriesAdapter.CategoryViewHolder> {

    private final List<ImageCategorySection> categories;
    private final List<ImagesAdapter> imagesAdapters;
    private final Context context;

    public interface OnAddImageListener {
        void onAddImage(ImageCategorySection section);
    }

    private final OnAddImageListener addImageListener;

    public ImageCategoriesAdapter(List<ImageCategorySection> categories, Context context, OnAddImageListener listener) {
        this.categories = categories;
        this.context = context;
        this.addImageListener = listener;
        this.imagesAdapters = new ArrayList<>();
        // יוצר Adapter פנימי קבוע לכל קטגוריה
        for (ImageCategorySection section : categories) {
            imagesAdapters.add(new ImagesAdapter(section.images));
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_category_section, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        ImageCategorySection section = categories.get(position);
        holder.txtTitle.setText(section.title);

        if (holder.recyclerImages.getLayoutManager() == null) {
            holder.recyclerImages.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            );
        }

        // Adapter פנימי קבוע לכל קטגוריה
        ImagesAdapter imagesAdapter = imagesAdapters.get(position);
        holder.recyclerImages.setAdapter(imagesAdapter);

        holder.btnAddImage.setOnClickListener(v -> addImageListener.onAddImage(section));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * עדכן קטגוריה מסוימת (רענן את התמונות הפנימיות)
     */
    public void notifyImageChanged(int categoryPosition) {
        if (categoryPosition >= 0 && categoryPosition < imagesAdapters.size()) {
            // עדכן את ה-adapter הפנימי
            ImagesAdapter adapter = imagesAdapters.get(categoryPosition);
            adapter.notifyDataSetChanged();

            // גם עדכן את הקטגוריה עצמה (למקרה שיש בעיה)
            notifyItemChanged(categoryPosition);
        }
    }

    /**
     * הוסף תמונה לקטגוריה ועדכן את ה-UI
     */
    public void addImageToCategory(int categoryPosition, Image image) {
        if (categoryPosition >= 0 && categoryPosition < categories.size()) {
            ImageCategorySection section = categories.get(categoryPosition);
            section.images.add(image);

            // עדכן את ה-adapter הפנימי
            ImagesAdapter adapter = imagesAdapters.get(categoryPosition);
            adapter.notifyItemInserted(section.images.size() - 1);
        }
    }

    /**
     * עדכן תמונה ספציפית בקטגוריה
     */
    public void updateImageInCategory(int categoryPosition, int imagePosition) {
        if (categoryPosition >= 0 && categoryPosition < imagesAdapters.size()) {
            ImagesAdapter adapter = imagesAdapters.get(categoryPosition);
            adapter.notifyItemChanged(imagePosition);
        }
    }

    /**
     * עדכן את כל הקטגוריות (אם יש שינוי כללי)
     */
    public void notifyAllImagesChanged() {
        for (ImagesAdapter adapter : imagesAdapters) {
            adapter.notifyDataSetChanged();
        }
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        RecyclerView recyclerImages;
        Button btnAddImage;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtCategoryTitle);
            recyclerImages = itemView.findViewById(R.id.recyclerImages);
            btnAddImage = itemView.findViewById(R.id.btnAddImage);
        }
    }
}
