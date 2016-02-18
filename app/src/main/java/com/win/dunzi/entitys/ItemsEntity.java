package com.win.dunzi.entitys;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * author：WangShuang
 * Date: 2016/1/27 15:52
 * email：m15046658245_1@163.com
 */

@Table(name = "ItemsEntity")
public class ItemsEntity extends Model implements Serializable {

    @Column(name = "Image")
    public String image;

    @Column(name = "User")
    public UserEntity user;

    @Column(name = "Pid")
    public int pid;

    @Column(name = "Votes")
    public VotesEntity votes;

    @Column(name = "Content")
    public String content;

    @Column(name = "Comments_count")
    public int comments_count;


    @Column(name = "Share_count")
    public int share_count;

    public ItemsEntity(String image, UserEntity user, int pid, VotesEntity votes, String content, int comments_count, int share_count) {
        super();
        this.image = image;
        this.user = user;
        this.pid = pid;
        this.votes = votes;
        this.content = content;
        this.comments_count = comments_count;
        this.share_count = share_count;
    }
    public ItemsEntity(){
        super();
    }

    @Table(name = "UserEntity")
    public static class UserEntity extends Model implements Serializable {
        @Column(name = "Login")
        public String login;

        @Column(name ="Pid")
       public int pid;

        @Column(name = "Icon")
        public String icon;

        public UserEntity(String login, int pid, String icon) {
            super();
            this.login = login;
            this.pid = pid;
            this.icon = icon;
        }

        public UserEntity(){
            super();
        }
    }

    @Table(name = "VotesEntity")
    public static class VotesEntity  extends Model implements Serializable {
        @Column(name = "Down")
        public int down;
        @Column(name = "Up")
        public int up;

        public VotesEntity(int down, int up) {
            super();
            this.down = down;
            this.up = up;
        }
        public VotesEntity(){
            super();
        }
    }
}


