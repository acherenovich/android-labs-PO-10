package com.example.lab2;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button; // Добавили импорт
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<Product> cartItems;
    private LayoutInflater inflater;
    private TextView totalPriceTextView; // Добавили ссылку на TextView

    // Измененный конструктор
    public CartAdapter(Context context, List<Product> cartItems, TextView totalPriceTextView) {
        this.context = context;
        this.cartItems = cartItems;
        this.inflater = LayoutInflater.from(context);
        this.totalPriceTextView = totalPriceTextView; // Сохраняем ссылку
        updateTotalPrice(); // Вызываем расчет при создании адаптера
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_cart, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.textViewCartItemName);
            holder.priceTextView = convertView.findViewById(R.id.textViewCartItemPrice);
            holder.quantityTextView = convertView.findViewById(R.id.textViewCartItemQuantity);
            holder.imageView = convertView.findViewById(R.id.imageViewCartItem);
            holder.deleteButton = convertView.findViewById(R.id.buttonDeleteItem); // Добавили
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = cartItems.get(position);

        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText("Цена: " + product.getPrice());
        holder.quantityTextView.setText("Кол-во: " + product.getQuantity());

        Glide.with(context)
                .load(product.getImagePath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .centerCrop())
                .into(holder.imageView);


        // Обработчик нажатия на кнопку "Удалить"
        holder.deleteButton.setOnClickListener(v -> {
            cartItems.remove(position); // Удаляем элемент из списка
            notifyDataSetChanged();     // Сообщаем адаптеру, что данные изменились
            updateTotalPrice();          // Пересчитываем итоговую стоимость
        });

        return convertView;
    }

    // Метод для расчета и обновления итоговой стоимости
    public void updateTotalPrice() {
        double total = 0;
        for (Product product : cartItems) {
            total += product.getPrice() * product.getQuantity();
        }
        totalPriceTextView.setText("Итого: " + String.format("%.2f", total)); // Форматируем вывод
    }


    static class ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        ImageView imageView;
        Button deleteButton; // Добавили
    }
}