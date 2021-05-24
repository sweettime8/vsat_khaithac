<template>
  <div class="app-container claim">
   
    <div class="filter-container">
        <el-row :gutter="20" >
            <el-col :span="6" :offset="1">
              <el-row >
               <h2 class="margin-top">DANH SÁCH NHÓM QUYỀN</h2>
               <template>
                 <el-select v-model="roleCodeSelect.roleCode" value-key="roleName"  @change="changeRole" >
                      <el-option v-for="item in list_role" :key="item.id" :label="item.roleCode" :value="item.roleCode"  :disabled="item.roleCode=='Administrator'">     
                     </el-option>
                   </el-select>

              </template>
              </el-row>
              <el-row >
                <br/><br/>
                  <el-button class="search" @click="addRole()" type="primary">Thêm</el-button>
                  <el-button v-model="roleCodeSelect.roleCode" class="search" @click="deleteRolePathPerMis()" type="primary">Xóa</el-button>
              </el-row>

            </el-col>
            <el-col :span="10"  style="margin-left:0">
                 <h2 class="margin-top">CHỨC NĂNG</h2>
                <el-form ref="RolePathForm" :model="rolePagePermission" :rules="rules" label-position="left" label-width="180px">
                    <el-form-item label="Tên nhóm quyền">
                        <el-input placeholder="Tên nhóm quyền" :maxlength="100" v-model="roleCodeSelect.roleCode"> </el-input>
                    </el-form-item>   
                    <el-form-item label="Mô tả nhóm quyền">
                        <el-input placeholder="Mô tả nhóm quyền" :maxlength="255" v-model="roleCodeSelect.roleDetail"> </el-input>
                    </el-form-item> 
                    <div class="body-table" style="overflow: auto; height:700px">
                    <el-table  v-loading="loading" :data="list_apiPath" border fit highlight-current-row style="width: 100%" >
                        <el-table-column align="center" v-if="!list_apiPath || !list_apiPath.length">
                            <span>Không có bản ghi</span>
                        </el-table-column>

                        <el-table-column align="center" label="STT" width="50">
                            <template slot-scope = "scope">
                                <span>{{ scope.$index + 1 }}</span>
                            </template>
                        </el-table-column>

                        <el-table-column label="Quyền">
                            <template slot-scope="scope">
                                <span>{{ scope.row.pageName }}</span>
                            </template>
                         </el-table-column>

                         <el-table-column label="Thêm" width="70">
                            <template slot-scope="scope">
                                 <el-checkbox :disabled="list_apiPath_FirstLoad[scope.$index].canCreate == 0" :value="scope.row.canCreate == 1" :checked="scope.row.canCreate == 1 && scope.row.roleCode != roleUserLogin" @change="updateDataCheckbox('CREATE',scope.row, $event)"></el-checkbox>
                            </template>
                         </el-table-column>
                         <el-table-column label="Sửa" width="70">
                            <template slot-scope="scope">
                                 <el-checkbox :disabled="list_apiPath_FirstLoad[scope.$index].canUpdate == 0" :value="scope.row.canUpdate == 1 " :checked="scope.row.canUpdate == 1 && scope.row.roleCode != roleUserLogin" @change="updateDataCheckbox('UPDATE',scope.row, $event)"></el-checkbox>
                            </template>
                         </el-table-column>
                         <el-table-column label="Xem" width="70">
                            <template slot-scope="scope">
                                 <el-checkbox :disabled="list_apiPath_FirstLoad[scope.$index].canRead == 0" :value="scope.row.canRead == 1"  :checked="scope.row.canRead == 1 && scope.row.roleCode != roleUserLogin" @change="updateDataCheckbox('READ',scope.row, $event)"></el-checkbox>
                            </template>
                         </el-table-column>
                         <el-table-column label="Xóa" width="70">
                            <template slot-scope="scope">
                                 <el-checkbox :disabled="list_apiPath_FirstLoad[scope.$index].canDelete == 0" :value="scope.row.canDelete == 1" :checked="scope.row.canDelete == 1 && scope.row.roleCode != roleUserLogin" @change="updateDataCheckbox('DELETE',scope.row, $event)"  ></el-checkbox>
                            </template>
                         </el-table-column>                                                                                                   

                     </el-table>
                     </div>
                     <br/><br/>
                  <el-button class="search" @click="createPermission()" type="primary">Đồng ý</el-button>
                  <el-button class="search" @click="clearForm()" type="primary">Làm lại</el-button>
                </el-form>       
            </el-col>
        </el-row>

    </div>
    
  </div>
</template>

<script>
import Resource from "@/api/resource";
import waves from "@/directive/waves"; // Waves directive`
import { mapGetters } from 'vuex';

const adminResource = new Resource("rbac/admin");
const roleCreateByUserResource = new Resource("rbac/role/role-creat-by-user");
const userResource = new Resource("user/cms");
const pageByRoleUserLoginResource = new Resource("rbac/role/page-permission-of-role");
const createUserRoleAndPermissionResource = new Resource("rbac/role/user/role-permission");
const deleteUserRoleAndPermissionResource = new Resource("rbac/role/user/role-permission");

export default {
  directives: { waves },
  data() {
    return {
      dialogVisible: false,
      addRoleCode: "",
      action :'INSERT_RULE',
      roleCodeSelect: {
         roleCode : "",
         roleName : "",
         roleDetail:""
      },
      newRole : "",
      roleUserLogin:"",
      roleUser: {
         roleCode : "",
         roleName : "",
      },
      default_role: {
        id: "",
        roleCode: "",
        roleName: "Chọn quyền",
        isAdmin: 0,
        isDelete: 0,
        createdAt: "",
      },
      rolePagePermission:{
          roleName:'',
          roleCode:'',
          pageUrl:'',
          pageName:'',
          canCreate: 0,
          canRead: 0,
          canUpdate : 0,
          canDelete : 0,
          isDelete : 0
      },
      addPagePermission:{
          roleName:'',
          roleCode:'',
          roleDetail:'',
          roleCodeOld:'',
          action:'',
          lstApiAddRole:null

      },
      list_role: null,
      list_apiPath: null,
      list_apiPath_FirstLoad: null,
      list_apiPathSelect: null,
      list_user: null,
      list: null,
      total: 0,
      loading: true,
      dialogLoading: false,
      dialogEdit: false,
      dialogCreate: false,
      reason: null,
      uuid: null,
      id: null,
      searchKeyword: null,
      selectUser: null,
      query: {
        currentPage: 1,
        rowsPerPage: 20,
        sort: "createdAt",
        keyword: this.searchKeyword,
        roleCode: this.roleCode,
      },

      rules: {
        selectUser: [
          {
            required: true,
            message: "Bắt buộc nhập user",
            trigger: "blur",
          },
        ],
        addRoleCode: [
          {
            required: true,
            message: "Bắt buộc nhập chọn quyền",
            trigger: "blur",
          },
        ],
      },
      attribute: {
        name: "",
        values: [
          {
            value: "",
          },
        ],
      }
    };
  },
 computed: {
     ...mapGetters([
      'userId',
      'userName',
      'name',
      'avatar',
      'roles',
    ]),
  },
  created() {
    //this.getList();
    this.getRoleListCreateByUserLogin();
    this.getListPageByRoleUserLogin();
  },
  
  methods: {
    async getListPageByRoleUserLogin(){
        console.log("mrd getListPageByRoleUserLogin1 : " + data);
        this.roleUserLogin = this.$store.getters.roles;
        console.log("mrd get role userlogin");


        const { data } = await pageByRoleUserLoginResource.list({
            roleCode : this.$store.getters.roles[0],
        });
        this.loading = true;
        this.list_apiPath =  data;
        this.list_apiPath_FirstLoad = JSON.parse(JSON.stringify(data));
        for (var index = 0; index < this.list_apiPath.length; index++) {
          this.list_apiPath[index].canCreate = 0;
          this.list_apiPath[index].canUpdate = 0;
          this.list_apiPath[index].canRead = 0;
          this.list_apiPath[index].canDelete = 0;
                
        }

        this.loading = false;
        console.log("mrd getListPageByRoleUserLogin : "+ this.$store.getters.roles);
    },

    changeRole(data) {
       this.action = 'UPDATE_RULE';
        for (var index = 0; index < this.list_role.length; index++) {
            if(this.list_role[index].roleCode == data ){
                this.roleCodeSelect.roleDetail = this.list_role[index].roleDetail;
                this.addPagePermission.roleCodeOld = this.list_role[index].roleCode;
            }              
        }

        pageByRoleUserLoginResource.list({roleCode : this.roleCodeSelect.roleCode }).then((response) => {
              if (response.status === 200) {
                  this.list_apiPathSelect = response.data;
                  //set all check list_apiPath = 0
                  for(var i = 0; i < this.list_apiPath.length; i++){
                       this.list_apiPath[i].canCreate =0;
                       this.list_apiPath[i].canUpdate = 0;
                       this.list_apiPath[i].canRead = 0;
                       this.list_apiPath[i].canDelete = 0;
                  }
                  
                  for (var index1 = 0; index1 < this.list_apiPathSelect.length; index1++) {

                    for(var index2 = 0; index2 < this.list_apiPath.length; index2++){

                      if((this.list_apiPath[index2].pageUrl == this.list_apiPathSelect[index1].pageUrl)
                          && (this.list_apiPath[index2].pageName == this.list_apiPathSelect[index1].pageName))
                          {
                              this.list_apiPath[index2].roleCode = this.list_apiPathSelect[index1].roleCode;
                              this.list_apiPath[index2].canCreate = this.list_apiPathSelect[index1].canCreate;
                              this.list_apiPath[index2].canUpdate = this.list_apiPathSelect[index1].canUpdate;
                              this.list_apiPath[index2].canRead = this.list_apiPathSelect[index1].canRead;
                              this.list_apiPath[index2].canDelete = this.list_apiPathSelect[index1].canDelete;
                          }
                    
                  }
                }

              }
            });

    },

    updateDataCheckbox: function(checkboxName,checked,e) {
        console.log("mrd checkboxName : " + checkboxName);
        
        for (var index = 0; index < this.list_apiPath.length; index++) {
          if(this.list_apiPath[index].pageUrl == checked.pageUrl && this.list_apiPath[index].pageName == checked.pageName){

              if(checkboxName == 'CREATE' && e == true){
                this.list_apiPath[index].canCreate  = 1;
                this.list_apiPath[index].canRead  = 1;
                
              }else if(checkboxName == 'CREATE' && e == false){
                this.list_apiPath[index].canCreate  = 0;
              }
              
              if(checkboxName == 'UPDATE' && e == true){
                this.list_apiPath[index].canUpdate  = 1;
                this.list_apiPath[index].canRead  = 1;
              }else if(checkboxName == 'UPDATE' && e == false){
                this.list_apiPath[index].canUpdate  = 0;
              }

              if(checkboxName == 'READ' && e == true){
                this.list_apiPath[index].canRead  = 1;
              }else if(checkboxName == 'READ' && e == false){
                this.list_apiPath[index].canRead  = 0;
              }

              if(checkboxName == 'DELETE' && e == true){
                this.list_apiPath[index].canDelete  = 1;
                this.list_apiPath[index].canRead  = 1;
              }else if(checkboxName == 'DELETE' && e == false){
                this.list_apiPath[index].canDelete  = 0;
              }
          }
                
        }
    },

    handleFilter() {
      this.getList();
    },
    showAddForm() {
      this.dialogVisible = true;
    },
    async getRoleListCreateByUserLogin(){
        const { data } = await roleCreateByUserResource.list();
        this.list_role = data;
        this.userNameLogin = this.$store.getters.userName;
    },

    async deleteRolePathPerMis(){
      var id = null;
      for (var index = 0; index < this.list_role.length; index++) {
            if(this.list_role[index].roleCode == this.roleCodeSelect.roleCode ){
               id =  this.list_role[index].id;
            }   
      
      }
     
      if(this.roleCodeSelect.roleCode){
        this.$confirm('Bạn có chắc chắn xóa quyền này không', 'Cảnh báo', {
          confirmButtonText: 'Đồng ý',
          cancelButtonText: 'Hủy bỏ',
          type: 'warning',
        })
        .then(() => {
          deleteUserRoleAndPermissionResource.destroy(id)
          .then(response => {
            if (response.status === 200 && response.data !=null) {
              this.$notify({
                title: 'Thành công',
                message: 'Xóa thành công',
                type: 'success'
              });
              this.getRoleListCreateByUserLogin();
              this.addRole();
            }else{
              this.$notify({
                title: "Xóa Lỗi",
                message: response.message,
                type: 'error'
              });
            }

          })
          .catch(err => {
              this.$notify({
              title: "Xóa Lỗi",
              message: err,
              type: "error",
            });
          })
        })
        .catch(() => {
          console.log('cancel');
        });
  
      }else{
            this.$notify({
              title: "Waring",
              message: "Cần chọn nhóm quyền để xóa",
              type: "warning",
            });
      }

    },


    async getListUser(keyword) {
      //console.log(keyword);
      if (keyword !== "" && keyword.length >= 3) {
        this.loading = true;
        let queryUser = {
          currentPage: 1,
          rowsPerPage: 10000,
          keyword: keyword,
          status: 1,
        };
        const { data } = await userResource.list(queryUser);
        this.list_user = data;
        this.loading = false;
      } else {
        this.list_user = [];
        this.selectUser = null;
      }
    },
    async createPermission(){
        console.log("createPermission 1: ");
        if(this.roleCodeSelect.roleCode != null && this.roleCodeSelect.roleCode.trim() != ""){
           console.log("createPermission 2: ");
              var lstApiPathRoleCreate = [];
              for (var index = 0; index < this.list_apiPath.length; index++) {
                console.log("createPermission 3: ");
                if(  this.list_apiPath[index].canCreate == 1 || this.list_apiPath[index].canUpdate == 1 
                  || this.list_apiPath[index].canDelete == 1  ||  this.list_apiPath[index].canRead == 1 )
                  {
                      console.log("createPermission 4: ");
                      lstApiPathRoleCreate.push(this.list_apiPath[index]);
                  }

              }
              if(lstApiPathRoleCreate.length == 0){
                      this.$notify({
                        title: "Cảnh báo",
                        message: "Bạn chưa chọn quyền nào!",
                        type: "warning",
                      });
              }else{
                  this.addPagePermission.roleName = this.roleCodeSelect.roleCode.trim();
                  this.addPagePermission.roleCode = this.roleCodeSelect.roleCode.trim();
                  if(this.roleCodeSelect.roleDetail != null ){
                    this.addPagePermission.roleDetail = this.roleCodeSelect.roleDetail.trim();
                  }else{
                     this.addPagePermission.roleDetail = '';
                  }

                  this.addPagePermission.action = this.action;
                  this.addPagePermission.lstApiAddRole = lstApiPathRoleCreate;
                
                  console.log("mrd this.addPagePermission.roleName : " + this.addPagePermission.roleName);
                  console.log("mrd this.addPagePermission.roleCode : " + this.addPagePermission.roleCode);
                  console.log("mrd this.addPagePermission.roleDetail : " + this.addPagePermission.roleDetail);
                  console.log("mrdthis.addPagePermission.lstApiAddRole.length : " + this.addPagePermission.lstApiAddRole.length);
                  for (var index1 = 0; index1 < this.addPagePermission.lstApiAddRole.length; index1++) {
                      console.log("createPermission : " + this.addPagePermission.lstApiAddRole[index1].pageName);       
                  }

                  createUserRoleAndPermissionResource.store(this.addPagePermission).then((response) => {
                      if (response.status === 200 && response.data != null) {
                          this.$notify({
                            title: "Thành Công",
                            message: "Thêm/sửa quyền thành công!",
                            type: "success",
                          });
                          this.getRoleListCreateByUserLogin();
                          this.addRole();
                        }else
                          this.$notify({
                            title: "Thất Bại",
                            message: response.message,
                            type: "error",
                          });
                      })
                      .catch((err) => {
                        console.log(err);
                  }); 
              }

        }else{
            this.$notify({
              title: "Waring",
              message: "Tên nhóm quyền không được để trống",
              type: "warning",
            });
        }

          
    },

   addRole(){
        for (var index = 0; index < this.list_apiPath.length; index++) {
          this.list_apiPath[index].roleCode = this.roleUserLogin;
          this.list_apiPath[index].canCreate = 0;
          this.list_apiPath[index].canUpdate = 0;
          this.list_apiPath[index].canRead = 0;
          this.list_apiPath[index].canDelete = 0;
                
        }
        this.action = 'INSERT_RULE';
        console.log("mrd[addRole] : "+ this.list_apiPath[0].roleCode);
        this.roleCodeSelect.roleCode = "";
        this.roleCodeSelect.roleDetail = "";
    },


    clearForm(){
        for (var index = 0; index < this.list_apiPath.length; index++) {
          this.list_apiPath[index].roleCode = this.roleUserLogin;
          this.list_apiPath[index].canCreate = 0;
          this.list_apiPath[index].canUpdate = 0;
          this.list_apiPath[index].canRead = 0;
          this.list_apiPath[index].canDelete = 0;
                
        }
  
        this.roleCodeSelect.roleCode = "";
        this.roleCodeSelect.roleDetail = "";
        this.action = 'INSERT_RULE'
    }


  },
};
</script>

<style lang="scss" scoped>
.margin-top {
  margin-top: 40px;
}
.search {
  margin-left: 15px;
}
.block {
  display: inline-block;
  //float: left;
  margin-right: 20px;
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

.guruInfor
{
  margin: 7px;
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
