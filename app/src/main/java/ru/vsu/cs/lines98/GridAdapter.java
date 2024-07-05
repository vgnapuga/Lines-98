package ru.vsu.cs.lines98;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private int[][] board;

    public GridAdapter(Context context, int[][] board) {
        this.context = context;
        this.board = board;
    }

    @Override
    public int getCount() {
        return 81;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item, parent, false);

            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int row = position / 9;
        int col = position % 9;
        int value = board[row][col];

        switch (value) {
            case 1:
                holder.imageView.setImageResource(R.drawable.ball_red);
                break;
            case 2:
                holder.imageView.setImageResource(R.drawable.ball_blue);
                break;
            case 3:
                holder.imageView.setImageResource(R.drawable.ball_green);
                break;
            case 4:
                holder.imageView.setImageResource(R.drawable.ball_yellow);
                break;
            case 5:
                holder.imageView.setImageResource(R.drawable.ball_purple);
                break;
            case 6:
                holder.imageView.setImageResource(R.drawable.ball_orange);
                break;
            default:
                holder.imageView.setImageResource(R.drawable.empty_cell);
                break;
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
    }

    public void updateBoard(int[][] newBoard) {
        this.board = newBoard;
        notifyDataSetChanged();
    }
}