<template>
  <div>
    <b-container fluid>
      <b-row>
        <b-col>
          <NavBar :username="loginUsername"/>
        </b-col>
      </b-row>

      <b-row class="mb-3">
        <b-col>
          <UserPageHeader :nickname="pageOwner.nickname"/>
        </b-col>
      </b-row>
    </b-container>

    <b-container>
      <b-row>
        <b-col sm="3" order-sm="1" order="2" cols="12">
          <InterestedUserList ref="interestedUserList" v-show="inOwnPage"/>
        </b-col>
        <b-col sm="9" order-sm="2" order="1" cols="12">
          <b-row class="mb-3">
            <b-col>
              <AddPost ref="addPost" @add-new-post="addNewPost" v-if="inOwnPage"/>
            </b-col>
          </b-row>
          <b-row v-for="post in posts" :key="post.id" class="mb-3">
            <b-col style="border: deepskyblue solid 1px">
              <Post :author="post.author.username" :date-created="post.timeCreated"
                    :show-author="false" @remove-post="removePost(post.id)" :inOwnPage="inOwnPage">
                {{ post.body }}
              </Post>
            </b-col>
          </b-row>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import axios from "axios";
import NavBar from "@/components/posts-page/NavBar";
import UserPageHeader from "@/components/posts-page/UserPageHeader";
import InterestedUserList from "@/components/posts-page/InterestedUserList";
import AddPost from "@/components/posts-page/AddPost";
import Post from "@/components/posts-page/Post";

export default {
  name: "Posts",
  components: {AddPost, InterestedUserList, UserPageHeader, NavBar, Post},
  data() {
    return {
      posts: null,
      interestedUsers: null,
      pageOwner: {
        username: null,
        password: null,
        email: null,
        nickname: null
      }
    };
  },

  created() {
    this.init();
  },

  computed: {
    //判断当前页面是否为当前登录用户的主页面
    inOwnPage() {
      return this.pageOwner.username === this.loginUsername;
    },

    loginUsername() {
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
      let self = this

      //获得当前用户的信息
      axios.get("/users/" + this.$route.params.username).then(response => {
        if (response.data.status === "USER_NOT_FOUND") {
          //若输入的url中的用户名不存在，则转到404页面
          self.$router.push("/404")
        } else if (response.data.status === "SUCCESS") {
          self.pageOwner = response.data.payload
        }

        //获取所有的posts
        axios.get("/posts/" + self.pageOwner.username).then(response => {
          //根据发布时间对posts进行排序
          self.posts = response.data.payload.sort((a, b) => {
            return new Date(b.timeCreated) - new Date(a.timeCreated)
          })
          //将所有post中的日期格式化
          self.posts.forEach(x => x.timeCreated =
              new Date(x.timeCreated).toLocaleString())
        })

        //获取所有可能感兴趣的用户
        axios.get("/users").then(response => {
          self.$refs.interestedUserList.interestedUsers
              = response.data.payload.filter(x =>
              x.username !== self.pageOwner.username);
        });
      })
    },

    addNewPost() {
      let self = this;
      const newPostBody = this.$refs.addPost.newPostBody.trim();
      if (newPostBody.length === 0) {
        alert("输入的内容不能为空");
        return;
      }

      axios.post("/posts/" + this.pageOwner.username, {
        timeCreated: new Date().toLocaleString(),
        body: newPostBody
      }).then(response => {
        if (response.data.status === "SUCCESS") {
          //格式化新post的日期
          response.data.payload.timeCreated =
              new Date(response.data.payload.timeCreated).toLocaleString();
          self.posts = [response.data.payload, ...self.posts];
          //成功添加post后清空输入框内容
          self.$refs.addPost.newPostBody = ""
        } else if (response.data.status === "USER_NOT_FOUND") {
          alert("添加失败");
        }
      })
    },

    removePost(postId) {
      let self = this;
      axios.delete("/posts/" + postId).then(response => {
        if (response.data.status === "SUCCESS") {
          self.posts = self.posts.filter(x => x.id !== postId);
        } else if (response.data.status === "POST_NOT_FOUND") {
          alert("删除失败");
        }
      })
    }
  }
}
</script>

<style scoped>
.container-fluid,
.col {
  padding-left: 0;
  padding-right: 0;
}

.row {
  margin-left: 0;
  margin-right: 0;
}
</style>