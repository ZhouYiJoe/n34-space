<template>
  <div id="posts">
    <NavBar :username="currentUsername"/>
    <div id="user-page-header">
      <UserPageHeader :username="this.$route.params.username"/>
    </div>
    <div id="interested-user-list-container">
      <InterestedUserList ref="interestedUserList"
                          v-show="inOwnPage"/>
    </div>
    <div id="add-post" v-if="inOwnPage">
      <AddPost ref="addPost" @add-new-post="addNewPost"/>
    </div>
    <div class="post" v-for="post in posts" :key="post.id">
      <Post :author="post.author.username"
            :date-created="post.timeCreated"
            :show-author="false"
            @remove-post="removePost(post.id)"
            :inOwnPage="inOwnPage">
        {{ post.body }}
      </Post>
    </div>
  </div>
</template>

<script>
import Post from "@/components/block/Post";
import UserPageHeader from "@/components/block/UserPageHeader";
import axios from "axios";
import AddPost from "@/components/block/AddPost";
import InterestedUserList from "@/components/block/interested-user-list/InterestedUserList";
import NavBar from "@/components/block/nav-bar/NavBar";

export default {
  name: "Posts",
  components: {NavBar, InterestedUserList, AddPost, UserPageHeader, Post},

  data() {
    return {
      posts: null,
      interestedUsers: null
    };
  },

  created() {
    this.init();
  },

  computed: {
    //判断当前页面是否为当前登录用户的主页面
    inOwnPage() {
      return this.$route.params.username ===
          localStorage.getItem("username");
    },

    currentUsername() {
      return localStorage.getItem("username");
    }
  },

  watch: {
    $route() {
      this.init();
    }
  },

  methods: {
    init() {
      let self = this;
      const username = this.$route.params.username;

      //获取所有的posts
      axios.get("/api/posts/" + username).then(response => {
        //若输入的url中的用户名不存在，则转到404页面
        if (response.data.userFound === false) {
          self.$router.replace("/404");
        } else {
          //根据发布时间对posts进行排序
          self.posts = response.data.posts.sort((a, b) => {
            return new Date(b.timeCreated) - new Date(a.timeCreated);
          });
          //将所有post中的日期格式化
          self.posts.forEach(x => x.timeCreated =
              new Date(x.timeCreated).toLocaleString());
        }
      });

      //获取所有可能感兴趣的用户
      axios.get("/api/users").then(response => {
          self.$refs.interestedUserList.interestedUsers
              = response.data.filter(x =>
              x.username !== localStorage.getItem("username"));
      });
    },

    addNewPost() {
      let self = this;
      const newPostBody = this.$refs.addPost
          .$refs.newPostInput.newPostBody.trim();
      if (newPostBody.length === 0) {
        alert("输入的内容不能为空");
        return;
      }
      const username = localStorage.getItem("username");

      axios.post("/api/posts/" + username, {
        timeCreated: new Date(),
        body: newPostBody
      }).then(response => {
        if (response.data != null) {
          //格式化新post的日期
          response.data.timeCreated =
              new Date(response.data.timeCreated).toLocaleString();
          self.posts = [response.data, ...self.posts];
          //成功添加post后清空输入框内容
          self.$refs.addPost
              .$refs.newPostInput.newPostBody = ""
        } else {
          alert("添加失败");
        }
      })
    },

    removePost(postId) {
      let self = this;
      axios.delete("/api/posts/" + postId).then(response => {
        if (response.data === true) {
          self.posts = self.posts.filter(x => x.id !== postId);
        } else {
          alert("删除失败");
        }
      })
    }
  }
}
</script>

<style scoped>
#posts {
  overflow: hidden;
  width: 100%;
  min-height: 100%;
  position: absolute;
  background-color: #f6fafd;
}

.post {
  width: fit-content;
  height: fit-content;
  margin: 5px auto;
  box-shadow: 0 6px 11px 0 rgba(0, 0, 0, 0.08) !important;
}

#add-post {
  box-shadow: 0 6px 11px 0 rgba(0, 0, 0, 0.04) !important;
  width: fit-content;
  margin: 0 auto;
}

#user-page-header {
  margin-bottom: 20px;
}

#interested-user-list-container {
  float: left;
  margin-left: 120px;
  box-shadow: 0 6px 11px 0 rgba(0, 0, 0, 0.04) !important;
}
</style>