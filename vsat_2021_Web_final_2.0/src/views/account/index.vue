<template>
  <div class="app-container">
    <h2 class="no-margin-top">Quản lý tài khoản</h2>
    <div class="filter-container">
      <template>
        <div class="block">
          <el-input v-model="query.keyword" placeholder="Tìm kiếm (email, mobile, name...)" style="width:250px"></el-input>
        </div>
        <div class="block">
          <el-select v-model="query.status" clearable placeholder="Trạng thái" @change="handleFilter()">
            <el-option v-for="item in listStatus" :key="item.key" :label="item.value" :value="item.key"></el-option>
          </el-select>
        </div>
        <el-button class="search" @click="handleFilter()" type="primary">Tìm kiếm</el-button>
        <el-button v-show="checkCreateRole" class="search" @click="handleUser(null)" type="primary">Thêm mới</el-button>
      </template>
    </div>

    <el-table
      v-loading="loading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column align="center" label="STT" width="50">
        <template slot-scope="scope">
          <span>{{
            (query.currentPage - 1) * query.rowsPerPage + scope.$index + 1
          }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Tên tài khoản" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.userName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Họ tên" >
        <template slot-scope="scope">
          <span>{{ scope.row.fullName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Quyền" width="250">
        <template slot-scope="scope">
          <span><el-tag effect="dark">{{ scope.row.role }}</el-tag></span>
        </template>
      </el-table-column>      

      <el-table-column label="Số điện thoại" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.mobile }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Trạng thái" width="150" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.status == 1"><el-tag effect="dark">Hoạt động</el-tag></span>
          <span v-else><el-tag type="danger" effect="dark" >Không hoạt động</el-tag></span>
        </template>
      </el-table-column>

      <el-table-column label="Người tạo" width="250">
        <template slot-scope="scope">
          <span>{{ scope.row.createdBy }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="Thời gian" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.createdAt | formatDate }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Tác vụ" align="center" width="200">
        <template slot-scope="scope">
          <span v-if="scope.row.userName != 'admin' && scope.row.createdBy == userNameLogin " ><el-button type="primary" size="small" style="margin-right: 5px" @click="handleUser(scope.row)">Sửa</el-button></span>        
          <el-button v-if="scope.row.userName != 'admin'" type="danger" size="small" @click="handleChangePass(scope.row.uuid)">Reset mật khẩu</el-button>

          <!-- form edit User -->
          <el-dialog top="5vh"  title="Thông tin tài khoản" :visible.sync="changeUserInfoVisiable" width="50%" :close-on-click-modal="false">
            <el-form ref="editUserForm" :model="editUser" :rules="rules" label-position="left" label-width="180px">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="Tên đăng nhập" prop="userName">
                    <el-input :disabled="editUser.uuid!==null" placeholder="Tên đăng nhập" v-model="editUser.userName" ></el-input>
                  </el-form-item> 
                  <el-form-item label="Số điện thoại" prop="mobile">
                    <el-input placeholder="Số điện thoại" v-model="editUser.mobile"></el-input>
                  </el-form-item>    
                  <el-form-item label="Ngày sinh" prop="birthDay">
                    <el-date-picker v-model="editUser.birthDay"  type="date" placeholder="Ngày sinh" format="dd-MM-yyyy" value-format="dd-MM-yyyy" style="float:left;width:100%"></el-date-picker>              
                  </el-form-item> 
                  <el-form-item label="Đơn vị" prop="unitName">
                    <el-input placeholder="Đơn vị" v-model="editUser.unitName"></el-input>
                  </el-form-item>                   
                  <el-form-item label="Phòng ban" prop="departmentName">
                    <el-input placeholder="Phòng ban" v-model="editUser.departmentName"></el-input>
                  </el-form-item> 
                  <el-form-item label="Trạng thái" prop="status">
                    <el-select v-model="editUser.status" value-key="editUser.status" placeholder="- Chọn trạng thái -" style="float:left;width:100%" >
                      <el-option v-for="item in listStatus" :key="item.key" :label="item.value" :value="item.key"></el-option>
                  </el-select>
                  </el-form-item> 
                  <el-form-item label="Quyền"  >
                    <el-select  v-model="roleUser.roleCode" placeholder="Chọn quyền"  style="float:left;width:100%" @change="changeRole()">
                      <el-option v-for="item in list_role" :key="item.id" :label="item.roleName" :value="item.roleCode" :disabled="item.roleCode=='Administrator'">
                        {{ item.roleName }}
                      </el-option>
                    </el-select>
                  </el-form-item>                    
                </el-col>

                <el-col :span="12">

                  <div style="margin-left:15px">
                    <el-form-item label="Họ tên" prop="fullName">
                      <el-input placeholder="Họ tên" v-model="editUser.fullName"></el-input>
                    </el-form-item> 
                    <el-form-item label="Email" prop="email">
                      <el-input placeholder="email" v-model="editUser.email"></el-input>
                    </el-form-item> 
                    <el-form-item label="Giới tính" prop="gender">
                      <el-select v-model="editUser.gender" value-key="editUser.gender" placeholder="- Chọn trạng thái -" style="float:left;width:100%" >
                        <el-option v-for="item in listGender" :key="item.key" :label="item.value" :value="item.key"></el-option>
                      </el-select>
                    </el-form-item>     
                    <el-form-item label="Chức vụ" prop="positionName">
                      <el-input placeholder="Chức vụ" v-model="editUser.positionName"></el-input>
                    </el-form-item>                         
                    <el-form-item label="Địa chỉ" prop="address">
                      <el-input placeholder="Địa chỉ" v-model="editUser.address"></el-input>
                    </el-form-item> 
 
                    <el-form-item label="Ghi chú" prop="description">
                      <el-input placeholder="Ghi chú" v-model="editUser.description"></el-input>
                    </el-form-item>                      
                  </div>

                </el-col>
              </el-row>            

            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="changeUserInfoVisiable = false">Bỏ qua</el-button>
              <el-button type="primary" @click="updateUserInfo()">Đồng ý</el-button>
            </div> 
        </el-dialog>

          <!-- form create User -->
          <el-dialog top="5vh" title="Thông tin tài khoản" :visible.sync="createUserInfoVisiable" width="50%" :close-on-click-modal="false">
            <el-form ref="createUserForm" :model="editUser" :rules="rules" label-position="left" label-width="150px">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="Tên đăng nhập" prop="userName">
                    <el-input :disabled="editUser.uuid!==null" placeholder="Tên đăng nhập" v-model="editUser.userName" ></el-input>
                  </el-form-item> 
                  <el-form-item label="Mật khẩu" prop="password" >
                    <el-input show-password max-length="50" placeholder="Mật khẩu" v-model="editUser.password"></el-input>
                  </el-form-item>
                  <el-form-item label="Số điện thoại" prop="mobile">
                    <el-input placeholder="Số điện thoại" v-model="editUser.mobile"></el-input>
                  </el-form-item>    
                  <el-form-item label="Ngày sinh" prop="birthDay">
                    <el-date-picker v-model="editUser.birthDay"  type="date" placeholder="Ngày sinh" format="dd-MM-yyyy" value-format="dd-MM-yyyy" style="float:left;width:100%"></el-date-picker>              
                  </el-form-item> 
                  <el-form-item label="Đơn vị" prop="unitName">
                    <el-input placeholder="Đơn vị" v-model="editUser.unitName"></el-input>
                  </el-form-item>                   
                  <el-form-item label="Phòng ban" prop="departmentName">
                    <el-input placeholder="Phòng ban" v-model="editUser.departmentName"></el-input>
                  </el-form-item> 
                  <el-form-item label="Trạng thái" prop="status">
                    <el-select v-model="editUser.status" value-key="editUser.status" placeholder="- Chọn trạng thái -" style="float:left;width:100%" >
                      <el-option v-for="item in listStatus" :key="item.key" :label="item.value" :value="item.key"></el-option>
                  </el-select>
                  </el-form-item> 
                  <el-form-item label="Quyền"  >
                    <el-select  v-model="roleUser.roleCode" placeholder="Chọn quyền"  style="float:left;width:100%" @change="changeRole()">
                      <el-option v-for="item in list_role" :key="item.id" :label="item.roleName" :value="item.roleCode" :disabled="item.roleCode=='Administrator'">
                        {{ item.roleName }}
                      </el-option>
                    </el-select>
                  </el-form-item>                    
                </el-col>

                <el-col :span="12">

                  <div style="margin-left:15px">
                    <el-form-item label="Họ tên" prop="fullName">
                      <el-input placeholder="Họ tên" v-model="editUser.fullName"></el-input>
                    </el-form-item> 
                    <el-form-item label="Nhập lại mật khẩu" prop="rePassword" >
                      <el-input  show-password max-length="50" placeholder="Nhập lại mật khẩu" v-model="editUser.rePassword"></el-input>
                    </el-form-item> 
                    <el-form-item label="Email" prop="email">
                      <el-input placeholder="email" v-model="editUser.email"></el-input>
                    </el-form-item> 
                    <el-form-item label="Giới tính" prop="gender">
                      <el-select v-model="editUser.gender" value-key="editUser.gender" placeholder="- Chọn trạng thái -" style="float:left;width:100%" >
                        <el-option v-for="item in listGender" :key="item.key" :label="item.value" :value="item.key"></el-option>
                      </el-select>
                    </el-form-item>     
                    <el-form-item label="Chức vụ" prop="positionName">
                      <el-input placeholder="Chức vụ" v-model="editUser.positionName"></el-input>
                    </el-form-item>                         
                    <el-form-item label="Địa chỉ" prop="address">
                      <el-input placeholder="Địa chỉ" v-model="editUser.address"></el-input>
                    </el-form-item> 
 
                    <el-form-item label="Ghi chú" prop="description">
                      <el-input placeholder="Ghi chú" v-model="editUser.description"></el-input>
                    </el-form-item>                      
                  </div>

                </el-col>
              </el-row>
         
            </el-form>

            <div slot="footer" class="dialog-footer">
              <el-button @click="createUserInfoVisiable = false">Bỏ qua</el-button>
              <el-button type="primary" @click="insertUserInfo()">Đồng ý</el-button>
            </div> 
        </el-dialog>

         <!-- form reset password -->
        <el-dialog title="Reset mật khẩu" :visible.sync="changePassVisible" width="50%" :close-on-click-modal="false">
            <el-form ref="changePassForm" :model="newPassword" :rules="rules" label-position="left" label-width="180px">
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


        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 10"
      :total="total"
      :page.sync="query.currentPage"
      :limit.sync="query.rowsPerPage"
      @pagination="getList"
    />

  </div>
</template>

<script>
import Pagination from "@/components/Pagination";
import Resource from "@/api/resource";
import waves from "@/directive/waves"; // Waves directive
import { validString } from "@/utils";
import { mapGetters } from 'vuex';

// const roleResource = new Resource("rbac/role");
const roleUserResource = new Resource("rbac/role/user");
const roleCreateByUserResource = new Resource("rbac/role/role-creat-by-user");
const passwordResource = new Resource("user/password");
const resetPassResource = new Resource("user/reset-password");
const createUserResource = new Resource("user");
const editUserResource = new Resource("user/edit-user");
const userResource = new Resource("user/cms");
const userStatusResource = new Resource("user/status");
const listRoleUserResource = new Resource("rbac/role/list-role-user");


export default {
  components: {
    Pagination,
  },
  directives: { waves },
  data() {
    return {
     roleCode: "",
     roleUser: {
       roleCode : "",
       roleName : "",
     },
     addRoleCode:"",
     updateRoleCode :"",
     userNameLogin:"",
     list_role: null,
     changeUserInfoVisiable: false,
     createUserInfoVisiable:false,
     editUser: {
        uuid :"",
        userName: "",
        fullName: "",
        mobile : "",
        gender : "",
        email: "",
        password:"",
        rePassword:"",
        roleCode:"",
        roleCodeOld:"",
        roleName:"",
        unitName: "",
        departmentName:"",
        positionName:"",
      },
     changePassVisible: false,
      newPassword: {
        uuid :"",
        newPassword: "",
        rePassword: "",
      },
      rules: {
        userName: [
          {
            required: true,
            message: "Tên đăng nhập bắt buộc nhập",
            trigger: "blur",
          },
        ],
        fullName: [
          {
            required: true,
            message: "Họ tên bắt buộc nhập",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "Mật khẩu bắt buộc nhập",
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
        mobile: [
          {
            required: true,
            message: "Số điện thoại bắt buộc nhập",
            trigger: "blur",
          },
        ],
        email: [
          {
            required: true,
            message: "Email bắt buộc nhập",
            trigger: "blur",
          },
        ],
        roleName:[
          {
            required: true,
            message: "Quyền bắt buộc nhập",
            trigger: "blur",
          },
        ],

      },
      list: null,
      lstRoleUser: null,
      total: 0,
      loading: true,
      query: {
        currentPage: 1,
        rowsPerPage: 15,
        sort: "createdAt",
        keyword: "",
        status: "",
        roleCode: this.roleCode,
      },
      listStatus: [
        {
          key: 0,
          value: "Không hoạt động",
        },
        {
          key: 1,
          value: "Hoạt động",
        },
      ],
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
      userDetail: {
        uuid: "",
        address: "",
        birthDay: "",
        gender: "",
      },
      uuid: null,
      checkEditRole : true,
      checkCreateRole : true,
      checkViewRole : true,
      checkDeleteRole : true,
    };
 
  },
  computed: {
     ...mapGetters([
      'userId',
      'userName',
      'name',
      'avatar',
      'roles',
      'roleMenuActions'
    ]),
  },
  created() {
    this.getRoleMenuPage();
    this.getList();
    this.getRoleListUserLogin();
  },
  methods: {
    async getRoleMenuPage(){
      console.log(this.$store.getters.roleMenuActions);
      for(var i=0; i<this.$store.getters.roleMenuActions.length;i++){
        if(this.$store.getters.roleMenuActions[i].pageUrl == "/account"){
          this.checkCreateRole = this.$store.getters.roleMenuActions[i].canCreate;
          this.checkEditRole = this.$store.getters.roleMenuActions[i].canUpdate;
          this.checkViewRole = this.$store.getters.roleMenuActions[i].canRead;
          this.checkDeleteRole = this.$store.getters.roleMenuActions[i].canDelete;
        }
      }
    },

    async getRoleListUserLogin() {
      const { data } = await roleCreateByUserResource.list();
      if(data){
         this.list_role = data;      
         this.roleUser.roleName = data[1].roleName;
         this.roleUser.roleCode = data[1].roleCode; 
      }else{
        console.log("mrd else getRoleListUserLogin : " + data );
      }
      this.userNameLogin = this.$store.getters.userName;
    },

    async getList() {
      this.loading = true;
      this.query.keyword = this.query.keyword.trim();
      const { data, total } = await userResource.list(this.query);

      // const { dataLstRoleUser } = await listRoleUserResource.list();
      let dataLstRoleUser = await listRoleUserResource.list();

      this.list = data;     
      this.lstRoleUser = dataLstRoleUser;

      for(var i = 0; i < this.list.length; i++){
        for(var j = 0 ; j < this.lstRoleUser.data.length ;j++){
          if(this.lstRoleUser.data[j].uuidUser == this.list[i].uuid){
            this.list[i].role = this.lstRoleUser.data[j].roleCode;
          }
        }
      }

      if (data) {
        this.total = total;
      }
      this.loading = false;
    },

    handleUser(data) {      
        if(data) { // Fill data into model for update row
          //this.editUser = data;         
          roleUserResource.get(data.uuid).then((response) => {
              if (response.status === 200) {
                this.roleUser = {
                  roleCode : response.data[0].roleCode.roleCode,
                  roleName : response.data[0].roleCode.roleName               
                };

                this.editUser =  {
                    uuid : data.uuid,
                    userName: data.userName,
                    fullName: data.fullName,
                    mobile : data.mobile,
                    email : data.email,
                    gender : data.gender,
                    roleCode: data.roleCode,
                    roleName:data.roleName,
                    unitName: data.unitName,
                    departmentName:data.departmentName,
                    positionName : data.positionName
                  };                
                this.editUser.roleCodeOld = response.data[0].roleCode.roleCode;
              }
            })
            .catch((err) => {
              console.log(err);
            });        
           this.changeUserInfoVisiable = true;

           this.editUser.isActive = data.isActive + '';
        }else
         
          if(this.list_role == null){
              this.editUser = {
              uuid: null, 
              userName: '',
              fullName: '',
              mobile: '', 
              email: '',
              birthDay: '', 
              gender : 0, 
              isActive: '1',
              unitName:'',
              positionName: '',
              departmentName: '',
              address: '',
              status:1,
              roleCode :'',
              roleName : ''   
            };
          }else{
              this.editUser = {
              uuid: null, 
              userName: '',
              fullName: '',
              mobile: '', 
              email: '',
              birthDay: '', 
              gender : 0, 
              isActive: '1',
              unitName:'',
              positionName: '',
              departmentName: '',
              address: '',
              status:1,
              roleCode : this.list_role.roleCode,
              roleName : this.list_role.roleName  
            };
            this.createUserInfoVisiable = true;
          }
         
         
        
    },
    updateUserInfo(){
      this.$refs.editUserForm.validate((valid) => {
        if (valid) {
          console.log("mrd updateUserInfo()");
          this.editUser.roleCode = this.roleUser.roleCode;
          if(this.editUser.fullName != null){
            this.editUser.fullName = this.editUser.fullName.trim();
          }
          if(this.editUser.mobile != null){
            this.editUser.mobile = this.editUser.mobile.trim();
          }
          if(this.editUser.unitName != null){
            this.editUser.unitName = this.editUser.unitName.trim();
          }
          if(this.editUser.departmentName != null){
            this.editUser.departmentName = this.editUser.departmentName.trim();
          }          
          if(this.editUser.positionName != null){
            this.editUser.positionName = this.editUser.positionName.trim();
          }      

          editUserResource.updateDucnh(this.editUser).then((response) => {
              if (response.status === 200 && response.data != null) {
                 for(var i = 0; i < this.list.length; i++){
                    if(this.editUser.uuid == this.list[i].uuid){
                        this.list[i].role = this.editUser.roleCode;
                    }
                 }

                 this.$notify({
                  title: 'Thành công',
                  message: 'Sửa thành công',
                  type: 'success'
                 });                 
                 this.changeUserInfoVisiable = false;
                 this.getList();
              }else
                 this.$notify({
                  title: "Có lỗi xảy ra",
                  message: response.message,
                  type: "error",
                });
            })
            .catch((err) => {
              console.log(err);
            }); 
        }
      });
    },

    async insertUserInfo(){
      this.$refs.createUserForm.validate((valid) => {
        if (valid) {        
          if(this.editUser.password != this.editUser.rePassword){
              this.$notify({
                title: "Có lỗi xảy ra",
                message: "Mật khẩu nhập lại không khớp",
                type: "error",
              });
          } else{
            console.log("mrd insertUserInfo()");
            this.editUser.roleCode = this.roleUser.roleCode;
            this.editUser.userName = this.editUser.userName.trim();
            this.editUser.fullName = this.editUser.fullName.trim();
            this.editUser.mobile = this.editUser.mobile.trim();
            this.editUser.unitName = this.editUser.unitName.trim();
            this.editUser.departmentName = this.editUser.departmentName.trim();
            
            createUserResource.store(this.editUser).then((response) => {
                if (response.status === 200 && response.data != null) {
                    this.$notify({
                        title: 'Thành công',
                        message: 'Thêm tài khoản thành công',
                        type: 'success'
                    });
                  this.createUserInfoVisiable = false;
                  this.getList();
                }else
                    this.$notify({
                      title: "Có lỗi xảy ra",
                      message: response.message,
                      type: "error",
                    });
              })
              .catch((err) => {
                console.log(err);
              });            
          }         
          
        }
      });
    },

    changeRole(){
      console.log("mrd changeRole() roleCode: "+ this.roleUser.roleCode);
     
    },


    handleChangePass(uuid) {
      console.log("handleChangePass : " + uuid)
        this.newPassword = {
          uuid: uuid,
          newPassword: "",
          rePassword: "",
        };
        this.changePassVisible = true;
        this.$nextTick(() => {
          this.$refs["changePassForm"].clearValidate();
        });
    },
    changePass() {
      this.$refs.changePassForm.validate((valid) => {
        if (valid) {
          resetPassResource
            .updateDucnh(this.newPassword)
            .then((response) => {
              if (response.status === 200 && response.data !== null) {
                this.$notify({
                  title: "Thành công",
                  message: "Đổi mật khẩu thành công.",
                  type: "success",
                });
                this.changePassVisible = false
              }else{
                console.log("mrd error")
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
    handleFilter() {
      if (validString(this.query.keyword)) {
        this.$message({
          message: "Từ khóa không được chưa ký tự đặc biệt.",
          type: "error",
          duration: 5 * 1000,
          showClose: true,
        });
        return;
      }
      this.query.keyword = this.query.keyword.trim();
      this.query.currentPage = 1;
      this.getList();
    },
    changeUserStatus(uuid, status) {
      const msg =
        status == 1
          ? "Bạn có chắc chắn khóa tài khoản ?"
          : "Bạn có chắc chắn mở khóa tài khoản ?";
      this.$confirm(msg, "Cảnh báo", {
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Hủy bỏ",
        type: "warning",
      })
        .then(() => {
          const params = {
            uuid: uuid,
            status: status == 1 ? -1 : 1,
          };
          userStatusResource
            .update(params)
            .then((response) => {
              if (response.status === 200) {
                this.$notify({
                  title: "Thành công",
                  message:
                    status == 1
                      ? "Khóa tài khoản thành công."
                      : "Mở khóa tài khoản thành công.",
                  type: "success",
                });
                this.getList();
              }
            })
            .catch((err) => {
              this.$notify({
                title: "Lỗi",
                message: err.response.data.message,
                type: "error",
              });
            });
        })
        .catch(() => {
          console.log("canceled");
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;
  .card-panel-col {
    margin-bottom: 30px;
  }
  .card-panel {
    padding-bottom: 25px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #000;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
    border-color: rgba(0, 0, 0, 0.05);
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-people {
        background: #40c9c6;
      }
      .icon-message {
        background: #36a3f7;
      }
      .icon-money {
        background: #f4516c;
      }
      .icon-shopping {
        background: #34bfa3;
      }
    }
    .icon-people {
      color: #40c9c6;
    }
    .icon-message {
      color: #36a3f7;
    }
    .icon-money {
      color: #f4516c;
    }
    .icon-shopping {
      color: #34bfa3;
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: left;
      font-weight: bold;
      margin: 25px 0 20px 15px;
      .card-panel-text {
        line-height: 18px;
        color: #000;
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }
    .card-panel-detail {
      float: left;
      width: 100%;
      padding: 0 15px;
      margin-bottom: 5px;
    }
    .el-tag {
      width: 100% !important;
      text-align: center !important;
    }
  }
}
.no-margin-top {
  margin-top: 0;
}
.search {
  margin-left: 15px;
}
.block {
  display: inline-block;
  //float: left;
  margin-right: 10px;
}
.blue {
  color: green;
}
.red {
  color: red;
}
.add-value {
  text-align: right;
}
.list-detail .item {
  margin-bottom: 10px;
  .label {
    display: inline-block;
    width: 100px;
  }
  img {
    max-width: 100%;
    display: inline-block;
  }
}

.el-form-item__label{
  width: 140px;
}


</style>
<style lang="scss">
.form-value {
  .el-input__inner {
    margin-bottom: 10px;
  }
}
.claim td .el-button {
  margin-left: 0;
}
</style>
