<template>
  <div class="navbar">
    <hamburger
      id="hamburger-container"
      :is-active="sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />

    <div class="right-menu">
      <template v-if="device !== 'mobile'">
        <el-tooltip
          :content="$t('navbar.size')"
          effect="dark"
          placement="bottom"
        >
          <!-- <size-select id="size-select" class="right-menu-item hover-effect" /> -->
        </el-tooltip>
      </template>

      <el-dropdown
        class="avatar-container right-menu-item hover-effect"
        trigger="click"
      >
        <div class="avatar-wrapper">
          <img :src="avatarLoad" class="user-avatar" v-if="avatar" />
          <img
            src="@/assets/images/avatar_default.jpg"
            class="user-avatar"
            v-else
          />
          <i class="el-icon-caret-bottom" />
        </div>
        
        <el-dropdown-menu slot="dropdown">
          <span @click="handleInfoUser">
            <el-dropdown-item>           
            <img :src="avatarLoad" class="user-avatar" style="width:40px; height:40px;border-radius:20px" v-if="avatar" />
            <img
              src="@/assets/images/avatar_default.jpg"
              class="user-avatar"
              v-else
            />
            {{name}}</el-dropdown-item>
          </span>  

          <router-link to="/">
            <el-dropdown-item divided>
              <i class="el-icon-ship"></i>{{ $t("navbar.dashboard") }}
            </el-dropdown-item>
          </router-link>
          <span @click="handleInfoUser">
            <el-dropdown-item> <i class="el-icon-ship"></i>Thông tin </el-dropdown-item>
          </span>          
          <span @click="handleChangePass">
            <el-dropdown-item> <i class="el-icon-ship"></i>Đổi mật khẩu </el-dropdown-item>
          </span>
          <el-dropdown-item divided>
            <span style="display: block" @click="logout">{{
              $t("navbar.logOut")
            }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog title="Đổi mật khẩu" :visible.sync="changePassVisible" width="50%" :close-on-click-modal="false">
      <el-form ref="changePassForm" :model="newPassword" :rules="rules" label-position="left" label-width="180px">
        <el-form-item label="Mật khẩu hiện tại" prop="curentPassword">
          <el-input placeholder="Nhập mật khẩu hiện tại" v-model="newPassword.curentPassword" show-password max-length="50"></el-input>
        </el-form-item>
        <el-form-item label="Mật khẩu mới" prop="newPassword">
          <el-input placeholder="Nhập mật khẩu mới" v-model="newPassword.newPassword" show-password max-length="50"></el-input>
        </el-form-item> 
        <el-form-item label="Nhập lại mật khẩu mới" prop="rePassword">
          <el-input placeholder="Nhập lại mật khẩu mới" v-model="newPassword.rePassword" show-password max-length="50"></el-input>
        </el-form-item>         
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="changePassVisible = false">Bỏ qua</el-button>
        <el-button type="primary" @click="changePass()">Đồng ý</el-button>
      </div> 
  </el-dialog>

  <el-dialog title="Thông tin người dùng" :visible.sync="changeInfoVisible" width="50%" :close-on-click-modal="false">
      <el-form ref="changeInfoForm" :model="newInfo" :rules="rules" label-position="left" label-width="180px">
        <el-row>
          <el-col :span="11">
            <label>Tài Khoản</label>
            <el-form-item  prop="userName" label-width="0">
              <el-input :disabled="true"  v-model="newInfo.userName" style="width :100%"></el-input>
            </el-form-item>  

            <label>Ngày sinh</label>
            <el-form-item prop="userName" label-width="0">
               <el-date-picker v-model="newInfo.birthDay"  type="date" placeholder="Ngày sinh" format="dd-MM-yyyy" value-format="dd-MM-yyyy" style="float:left;width:100%"></el-date-picker>     
            </el-form-item>  

            <label>Email</label>
            <el-form-item prop="email" label-width="0">
              <el-input  v-model="newInfo.email" style="width :100%"></el-input>
            </el-form-item> 

            <label>Giới tính</label>
            <el-select v-model="newInfo.gender" value-key="editUser.gender" placeholder="- Chọn trạng thái -" style="float:left;width:100%" >
                <el-option v-for="item in listGender" :key="item.key" :label="item.value" :value="item.key"></el-option>
            </el-select>    
          </el-col>

          <el-col :span="11" style="float:right">
            <label>Họ tên</label>
            <el-form-item prop="fullName" label-width="0">
              <el-input :disabled="true" v-model="newInfo.fullName" style="width :100%"></el-input>
            </el-form-item>  
            
            <label>Số điện thoại</label>
            <el-form-item prop="mobile" label-width="0">
              <el-input  v-model="newInfo.mobile" style="width :100%"></el-input>
            </el-form-item>  

            <label>Địa chỉ</label>
            <el-form-item prop="address" label-width="0">
              <el-input  v-model="newInfo.address" style="width :100%"></el-input>
            </el-form-item> 

            <label>Ảnh đại diện</label>
            <el-form-item prop="avatar" label-width="0">
                    <img class="image" v-if="avatarUpload" :src="avatarUpload" style="max-width: 100%"/>
                    <el-button type="primary" style="display: block" @click="imagecropperShow=true">
                      Upload avatar
                    </el-button>
                    <image-cropper
                      v-show="imagecropperShow"
                      :key="imagecropperKey"
                      :width="200"
                      :height="200"
                      field="file"
                      url="upload/user/avatar"
                      lang-type="vi"
                      @close="closeChangeAvatar"
                      @crop-upload-success="cropSuccess"
                    />
            </el-form-item>  

          </el-col>
        </el-row>
 
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="changeInfoVisible = false">Bỏ qua</el-button>
        <el-button type="primary" @click="changeInfo()">Cập nhật</el-button>
      </div> 
  </el-dialog>

  </div>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";
import Breadcrumb from "@/components/Breadcrumb";
import Hamburger from "@/components/Hamburger";
import Resource from "@/api/resource";
import ImageCropper from '@/components/ImageCropper';

const passwordResource = new Resource("user/password");
const userInfoResource = new Resource("user");

var stoteFile = process.env.VUE_APP_FILE_API + "upload";

export default {
  components: {
    Breadcrumb,
    Hamburger,
    ImageCropper,
  },
  data() {
    return {
      avatarLoad : process.env.VUE_APP_FILE_API + "upload" + this.$store.getters.avatar,
      imagecropperShow: false,
      imagecropperKey: 0,
      changePassVisible: false,
      changeInfoVisible:false,
      avatarUpload : "",
      newPassword: {
        curentPassword: "",
        newPassword: "",
        rePassword: "",
      },
      newInfo:{
        userName :"",
        fullName : "",
        birthDay :"",
        mobile : "",
        email :"",
        address :"",
        gender :"",
        avatar :""
      },
      listGender: [
        {
          key: 0,
          value: "Nam",
        },
        {
          key: 1,
          value: "Nữ",
        },
        {
          key: 2,
          value: "Không xác định",
        },
      ],
      rules: {
        curentPassword: [
          {
            required: true,
            message: "Mật khẩu hiện tại bắt buộc nhập",
            trigger: "blur",
          },
        ],
        newPassword: [
          {
            required: true,
            message: "Mật khẩu mới bắt buộc nhập",
            trigger: "blur",
          },
        ],
        rePassword: [
          {
            required: true,
            message: "Mật khẩu nhập lại bắt buộc nhập",
            trigger: "blur",
          },
        ],
      },
    };
  },
  computed: {
    ...mapGetters(["sidebar", "name", "avatar", "device", "userId","address","birthDay","gender","mobile","email"]),
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar");
    },
    async logout() {
      await this.$store.dispatch("user/logout");
      this.$router.push(`/login`);
    },
    handleChangePass() {
      this.newPassword = {
        curentPassword: "",
        newPassword: "",
        rePassword: "",
      };
      this.changePassVisible = true;
      this.$nextTick(() => {
        this.$refs["changePassForm"].clearValidate();
      });
    },
    handleInfoUser(){
      console.log("handleInfoUser")
       this.newInfo = {
        userName : this.$store.getters.userName,
        fullName : this.$store.getters.name,
        birthDay :this.$store.getters.birthDay,
        mobile :this.$store.getters.mobile,
        email :this.$store.getters.email,
        address :this.$store.getters.address,
        gender :this.$store.getters.gender,
        avatar :this.$store.getters.avatar
      };
      this.changeInfoVisible = true;
    },
    changeInfo(){
      console.log("changeInfo")
      console.log(this.avatar)
      this.newInfo.avatar = this.avatarUpload.substring(stoteFile.length,this.avatarUpload.length);


      this.$refs.changeInfoForm.validate((valid) => {
        if (valid) {
          userInfoResource
            .updateDucnh(this.newInfo)
            .then((response) => {
              if (response.status === 200) {
                this.$notify({
                  title: "Thành công",
                  message: "Cập nhật thông tin thành công .",
                  type: "success",
                });
                this.logout();
              }
            })
            .catch((err) => {
              console.log(err);
            });
        }
      });      

    },


    changePass() {
      this.$refs.changePassForm.validate((valid) => {
        if (valid) {
          passwordResource
            .updateDucnh(this.newPassword)
            .then((response) => {
              if (response.status === 200 && response.data != null) {
                this.$notify({
                  title: "Thành công",
                  message: "Đổi mật khẩu thành công.",
                  type: "success",
                });
                this.logout();
              }else{
                this.$notify({
                  title: "Có lỗi xảy ra",
                  message: response.message,
                  type: "error",
                });
              }
            })
            .catch((err) => {
              console.log(err);
            });
        }
      });
    },
    cropSuccess(resData) {
      console.log("cropSuccess")
      this.avatarUpload = resData.data[0].fileDownloadUri;
      this.$store.state.avatar = this.avatarUpload;
    },
    closeChangeAvatar() {
        this.imagecropperShow = false;
    },

  },
};
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #0d518c;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding:0;
  display: block;
  margin-bottom: 0;
  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: -4px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 4px;
        }

        .el-icon-caret-bottom {
          color: #fff;
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
