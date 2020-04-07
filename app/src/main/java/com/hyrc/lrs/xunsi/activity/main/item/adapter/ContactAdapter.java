package com.hyrc.lrs.xunsi.activity.main.item.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.bean.TopUser;
import com.hyrc.lrs.xunsi.bean.User;
import com.hyrc.lrs.xunsi.utils.pinyin.PinyinUtils;
import com.hyrc.lrs.xunsi.view.IndexView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_TOP = 0;
    public static final int TYPE_START = 1;
    public static final int TYPE_ITEM = 2;
    public static final int TYPE_BOTTOM = 3;
    private Context context;

    public ContactAdapter(Context context) {
        this.context = context;
    }

    /**
     * 顶部“新的朋友”之类的总数
     */
    private List<TopUser> topList = new ArrayList<>();
    /**
     * 底部
     */
    private int bottomCount = 1;

    /**
     * 数据列表，
     */
    private List<User> dataList = new ArrayList<>();
    /**
     * 星标朋友数据列表
     */
    private List<User> starList = new ArrayList<>();
    /**
     * 返回的首字母列表
     */
    private List<String> firstWordList = new ArrayList<>();
    /**
     * 点击监听
     */
    private OnClickListener listener;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_TOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_top, parent, false);
                return new TopViewHolder(view);
            case TYPE_BOTTOM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_bottom, parent, false);
                return new BottomViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
                return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopViewHolder) {
            bindTopViewHolder((TopViewHolder) holder, position);
        } else if (holder instanceof ItemViewHolder) {
            bindItemViewHolder((ItemViewHolder) holder, position);
        } else if (holder instanceof BottomViewHolder) {
            ((BottomViewHolder) holder).textView.setText(String.format(Locale.getDefault(), "%d位联系人", dataList.size()));
        }
    }

    private void bindTopViewHolder(TopViewHolder holder, final int position) {
        if (topList != null && topList.size() > 0) {
            holder.textView.setText(topList.get(position).getUserName());
            //处理子项底部分割线
            holder.imageView.setImageResource(topList.get(position).getUserURL());
            //事件监听
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, position, TYPE_TOP);
                }
            });
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(v, position, TYPE_TOP);
                    return true;
                }
            });
            if (position + 1 == topList.size()) {
                holder.item_bottom.setVisibility(View.GONE);
            }
        }
    }

    private void bindItemViewHolder(final ItemViewHolder holder, final int position) {
        //有星标朋友的列表时
        if (starList.size() > position) {
            boolean isItemTop = position == 0;
            holder.textView.setText(starList.get(position).getUserName());
            //控制子项的索引项是否显示
            setIndexViewIsShow(holder, isItemTop);
            //设置子项的索引项的字符
            if (isItemTop) {
                holder.headTextView.setText("星标朋友");
            }
            //处理子项底部分割线
            holder.bottomLine.setVisibility((position == starList.size() - 1) ? View.GONE : View.VISIBLE);

            //事件监听
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, position - topList.size(), TYPE_START);
                }
            });
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(v, position - topList.size(), TYPE_START);
                    return true;
                }
            });

        } else {
            //去除星标
            int hasStatListPosition = position - topList.size() - starList.size();
            if (firstWordList.size() > 0 && hasStatListPosition < firstWordList.size()) {
                boolean isItemTop = firstWordList.indexOf(firstWordList.get(hasStatListPosition)) == hasStatListPosition;
                holder.textView.setText(dataList.get(hasStatListPosition).getUserName());
                //设置图片圆角角度
                RoundedCorners roundedCorners = new RoundedCorners(12);
                //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
                Glide.with(context).load(dataList.get(hasStatListPosition).getUserURL()).apply(options).into(holder.imageView);
                //控制子项的索引项是否显示
                setIndexViewIsShow(holder, isItemTop);
                //设置子项的索引项的字符
                if (isItemTop) {
                    holder.headTextView.setText(firstWordList.get(hasStatListPosition));
                }
                //处理子项底部分割线
                int state = (firstWordList.lastIndexOf(firstWordList.get(hasStatListPosition)) == hasStatListPosition) ? View.GONE : View.VISIBLE;
                holder.bottomLine.setVisibility(state);
            }
            //事件监听
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, position - topList.size() - starList.size(), TYPE_ITEM);
                }
            });
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(v, position - topList.size() - starList.size(), TYPE_ITEM);
                    return true;
                }
            });
        }
    }

    /**
     * 控制子项的索引项是否显示
     */
    private void setIndexViewIsShow(ItemViewHolder holder, boolean show) {
        int viewState = (show) ? View.VISIBLE : View.GONE;

        holder.headTextView.setVisibility(viewState);
        holder.headTopLine.setVisibility(viewState);
        holder.headBottomLine.setVisibility(viewState);
    }

    @Override
    public int getItemCount() {
        return topList.size() + starList.size() + dataList.size() + bottomCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < topList.size()) {
            return TYPE_TOP;
        } else if (position < topList.size() + starList.size()) {
            return TYPE_START;
        } else if (position == topList.size() + starList.size() + dataList.size() + bottomCount - 1) {
            return TYPE_BOTTOM;
        } else {
            return TYPE_ITEM;
        }
    }

    /**
     * 实际使用中数据应该是json，包括联系人姓名，头像图片地址等信息，应改成List<xxxbean>
     * 星标朋友应该是一个标记位，通过这个组成starList
     */
    public void setData(List<TopUser> TopData, List<User> StartData, List<User> itemData) {
        //置顶
        if (TopData != null && TopData.size() > 0) {
            this.topList = TopData;
        }
        //标星
        if (StartData != null && StartData.size() > 0) {
            this.starList = StartData;
        }
        //item数据
        if (itemData != null && itemData.size() > 0) {
            this.dataList = itemData;
            if (firstWordList != null && firstWordList.size() > 0) {
                firstWordList.clear();
            }
            //初始化 字母
            for (User name : itemData) {
                String firstWord = PinyinUtils.getSurnameFirstLetter(name.getUserName());
                if (firstWord != null) {
                    firstWordList.add(firstWord.toUpperCase());
                }
            }
        }
    }

    /**
     * @param word 索引列表划到的单词
     * @return 划到的单词第一次出现的位置
     */
    public int getFirstWordListPosition(String word) {
        //索引列表划到↑
        if (word.equals(IndexView.words[0])) {
            return 0;
        }
        //索引列表划到☆
        else if (word.equals(IndexView.words[1]) && starList.size() > 0) {
            return topList.size();
        } else if (firstWordList.indexOf(word) >= 0) {
            return firstWordList.indexOf(word) + topList.size() + starList.size();
        }
        return -1;
    }

    public void clearData() {
        if (firstWordList != null && firstWordList.size() > 0) {
            firstWordList.clear();
        }
        if (dataList != null && dataList.size() > 0) {
            dataList.clear();
        }
        if (starList != null && starList.size() > 0) {
            starList.clear();
        }
        if (topList != null && topList.size() > 0) {
            topList.clear();
        }
    }

    class TopViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private LinearLayout linearLayout;
        private View item_bottom;

        TopViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_iv_qz);
            textView = itemView.findViewById(R.id.item_tv_qz);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            item_bottom = itemView.findViewById(R.id.item_bottom);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView, headTextView;
        private ImageView imageView;
        private View bottomLine, headBottomLine, headTopLine;
        private LinearLayout linearLayout;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_tv);
            headTextView = itemView.findViewById(R.id.item_head_tv);
            imageView = itemView.findViewById(R.id.item_iv);
            bottomLine = itemView.findViewById(R.id.item_bottom);
            headBottomLine = itemView.findViewById(R.id.item_head_bottom);
            headTopLine = itemView.findViewById(R.id.item_head_top);
            linearLayout = itemView.findViewById(R.id.item_cl);
        }
    }

    class BottomViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        BottomViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_bottom_tv);
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {

        /**
         * 单击监听
         *
         * @param view     v
         * @param position position
         */
        void onClick(View view, int position, int type);

        /**
         * 长按监听
         *
         * @param view     v
         * @param position position
         */
        void onLongClick(View view, int position, int type);
    }
}
