<template>
  <div class="app-container">
    <h2 class="no-margin-top">Đối tượng không xác định</h2>
    <div class="filter-container">
      <el-row :gutter="20" >
        <div style="float:right">
            <template>            
              <div class="block">
                <el-input v-model="query.keyword" placeholder="Tìm kiếm (Tên, quốc tịch...)" style="width :400px"></el-input>
              </div>    
              <el-button class="search" @click="handleFilter()" type="primary">Tìm kiếm</el-button>
              <el-button v-show="checkCreateRole" class="search" @click="handleAddObject(null)" type="primary">Thêm mới</el-button>         
          </template>
        </div>
      </el-row>
    
    </div>

    <el-table
      v-loading="loading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column align="center" label="#" width="50">
        <template slot-scope="scope">
          <span>{{
            (query.currentPage - 1) * query.rowsPerPage + scope.$index + 1
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="ID" width="280">
          <template slot-scope="scope">
               <span>{{ scope.row.uuid }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Tên">
          <template slot-scope="scope" >
               <span>{{ scope.row.name }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Quốc tịch" width="150">
          <template slot-scope="scope">
               <span>{{ scope.row.countryName }}</span>
          </template>
      </el-table-column>
      <el-table-column label="Chiều dài" width="100"> 
          <template slot-scope="scope">
               <span>{{ scope.row.chieudai }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Chiều rộng" width="100"> 
          <template slot-scope="scope">
               <span>{{ scope.row.chieurong }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Tác động" width="140">
          <template slot-scope="scope">      
            <el-button v-show="checkEditRole" type="primary" size="small" @click="handleChangeObject(scope.row)">Sửa</el-button>
            <el-button v-show="checkDeleteRole" type="danger" size="small" @click="handleDeleteObject(scope.row.uuid)">Xóa</el-button>
          </template>
      </el-table-column>

    </el-table>

      <!-- form add Object -->
      <el-dialog top="5vh"  title="Thông tin" :visible.sync="addObjectVisiable" width="50%" :close-on-click-modal="false" >
        <el-form ref="ObjectUndefineAddForm" :model="addObject" :rules="rules" label-position="left" label-width="180px" class="frmAddObjectUndefine">
          <el-tabs v-model="activeNameFormEdit" @tab-click="handleClick">
            <el-tab-pane label="Thông tin chung" name="first">  
              <el-row>
                <el-col :span="12" style="padding:5px">
                  <label>UUID</label>
                  <el-form-item prop="uuid" label-width="0"><br/>
                    <el-input :disabled="true" placeholder="uuid" v-model="addObject.uuid"></el-input>
                  </el-form-item> 
                </el-col>  
                <el-col :span="12" style="padding:5px">
                  <label>Tên</label>
                  <el-form-item prop="name" label-width="0"><br/>
                    <el-input placeholder="Tên" v-model="addObject.name"></el-input>
                  </el-form-item> 
                </el-col> 
              </el-row> 

              <el-row>
                  <label>Quốc tịch</label>
                  <el-form-item prop="country" label-width="0"><br/>
                    <el-select  v-model="countryInfo.id" filterable placeholder="Chọn quốc tịch"  @change="changeCountry()" style="width :100%">
                        <el-option v-for="item in list_country" :key="item.id" :label="item.name" :value="item.id" >                          
                        </el-option>
                    </el-select>
                  </el-form-item> 
              </el-row>  

              <el-row>
                <el-col :span="12" style="padding:5px">
                  <label>Chiều dài (mét)</label>
                  <el-form-item prop="width" label-width="0"><br/>
                    <el-input-number v-model="addObject.chieudai" style="with:100%"></el-input-number>
                  </el-form-item> 
                  <label>IP nguồn</label>
                  <el-form-item prop="sourceIP" label-width="0"><br/>
                    <el-input placeholder="IP nguồn" v-model="addObject.sourceIP"></el-input>
                  </el-form-item> 
                  <label>Port nguồn</label>
                  <el-form-item prop="height" label-width="0" ><br/>
                    <el-input-number v-model="addObject.sourcePort" style="with:100%"></el-input-number>                  
                  </el-form-item>  
                </el-col> 

                <el-col :span="12" style="padding:5px">
                  <label>Chiều rộng (mét)</label>
                  <el-form-item prop="height" label-width="0" ><br/>
                    <el-input-number v-model="addObject.chieurong" style="with:100%"></el-input-number>                  
                  </el-form-item> 
                  <label>IP đích</label>
                  <el-form-item prop="destIP" label-width="0"><br/>
                    <el-input placeholder="IP đích" v-model="addObject.destIP"></el-input>
                  </el-form-item>  
                 <label>Port đích</label>
                  <el-form-item prop="height" label-width="0" ><br/>
                    <el-input-number v-model="addObject.destPort" style="with:100%"></el-input-number>                  
                  </el-form-item>                   
                </el-col> 
              </el-row>                
            </el-tab-pane>

            <!-- <el-tab-pane label="Ảnh đại diện" name="fourth">
                <el-form-item label="Avatar" prop="avatar">
                    <img class="image" v-if="addObject.imagePath" style="max-width: 100%"/>
                    <el-button type="primary" style="display: block" @click="imagecropperShow=true">
                      Upload avatar
                    </el-button>
                    <image-cropper
                      v-show="imagecropperShow"
                      :key="imagecropperKey"
                      :width="200"
                      :height="200"
                      field="file"
                      url="upload/contact/object"
                      lang-type="vi"
                      @close="closeChangeAvatar"
                    />
                </el-form-item>              
            </el-tab-pane> -->



          </el-tabs>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addObjectVisiable = false">Bỏ qua</el-button>
          <el-button type="primary" @click="addObjectInfo()">Đồng ý</el-button>
        </div>  
    </el-dialog>         

      <!-- form edit Object -->
      <el-dialog top="5vh"  title="Thông tin" :visible.sync="changeObjectVisiable" width="50%" :close-on-click-modal="false" >
        <el-form ref="ObjectUndefineEditForm" :model="editObject" :rules="rules" label-position="left" label-width="180px" class="frmObjectUndefine">
          <el-tabs v-model="activeNameFormEdit" @tab-click="handleClick">
            <el-tab-pane label="Thông tin chung" name="first">  
              <el-row>
                <el-col :span="12" style="padding:5px">
                  <label>UUID</label>
                  <el-form-item prop="uuid" label-width="0"><br/>
                    <el-input :disabled="true" placeholder="uuid" v-model="editObject.uuid"></el-input>
                  </el-form-item> 
                </el-col>  
                <el-col :span="12" style="padding:5px">
                  <label>Tên</label>
                  <el-form-item prop="name" label-width="0"><br/>
                    <el-input placeholder="Tên" v-model="editObject.name"></el-input>
                  </el-form-item> 
                </el-col> 
              </el-row> 

              <el-row>
                  <label>Quốc tịch</label>
                  <el-form-item prop="country" label-width="0"><br/>
                    <el-select  v-model="countryInfo.id" filterable placeholder="Chọn quốc tịch"  @change="changeCountry()" style="width :100%">
                        <el-option v-for="item in list_country" :key="item.id" :label="item.name" :value="item.id" >                          
                        </el-option>
                    </el-select>
                  </el-form-item> 
              </el-row>  

              <el-row>
                <el-col :span="12" style="padding:5px">
                  <label>Chiều dài (mét)</label>
                  <el-form-item prop="width" label-width="0"><br/>
                    <el-input-number v-model="editObject.chieudai" style="with:100%"></el-input-number>
                  </el-form-item>
                  <label>IP nguồn</label>
                  <el-form-item prop="sourceIP" label-width="0"><br/>
                    <el-input placeholder="IP nguồn" v-model="editObject.sourceIP"></el-input>
                  </el-form-item> 
                 <label>Port nguồn</label>
                  <el-form-item prop="height" label-width="0" ><br/>
                    <el-input-number v-model="editObject.sourcePort" style="with:100%"></el-input-number>                  
                  </el-form-item>  
                </el-col> 

                <el-col :span="12" style="padding:5px">
                  <label>Chiều rộng (mét)</label>
                  <el-form-item prop="height" label-width="0" ><br/>
                    <el-input-number v-model="editObject.chieurong" style="with:100%"></el-input-number>                  
                  </el-form-item> 
                  <label>IP đích</label>
                  <el-form-item prop="destIP" label-width="0"><br/>
                    <el-input placeholder="IP đích" v-model="editObject.destIP"></el-input>
                  </el-form-item>  
                 <label>Port đích</label>
                  <el-form-item prop="height" label-width="0" ><br/>
                    <el-input-number v-model="editObject.destPort" style="with:100%"></el-input-number>                  
                  </el-form-item>                     
                </el-col> 
              </el-row>                
            </el-tab-pane>

            <!-- <el-tab-pane label="Ảnh đại diện" name="fourth">
                <el-form-item label="Avatar" prop="avatar">
                    <img class="image" v-if="editObject.imagePath" style="max-width: 100%"/>
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
                    />
                </el-form-item>              
            </el-tab-pane> -->

            <el-tab-pane label="Danh mục IP" name="Five">
              <el-row>  
                <el-col :span="15" style="padding:5px">
                  <el-table v-loading="loading" :data="ips" border fit highlight-current-row style="width: 100%">
                      <el-table-column align="center" label="STT" width="50">
                      <template slot-scope="scope">
                        <span>{{
                          (query.currentPage - 1) * query.rowsPerPage + scope.$index + 1
                        }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="IP" width="200">
                      <template slot-scope="scope">
                        <span>{{ scope.row.ipAddress }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="Ghi chú" >
                      <template slot-scope="scope">
                        <span>{{ scope.row.note }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="Tác vụ" align="center" width="80">
                      <template slot-scope="scope">                           
                        <el-button type="danger" size="small" @click="handleDeleteIP(scope.row.ipAddress)">Xóa</el-button>
                      </template>
                    </el-table-column>
                    
                </el-table>
                <pagination
                  v-show="totalIP > 10"
                  :total="totalIP"
                  :page.sync="query.currentPage"
                  :limit.sync="query.rowsPerPage"
                  @pagination="getIPByMMSI"
                />
                </el-col>

                <el-col :span="9" style="padding:5px">
                  <el-form ref="addIpAddressForm" :model="addIpAddress" :rules="rules" label-position="left" label-width="180px" class="frmaddIP">
                    <label>IP</label>
                    <el-form-item prop="imo" label-width="0" ><br/>
                      <el-input v-model="addIpAddress.ip_address" style="with:100%"></el-input>                  
                    </el-form-item> 
                    <label>Ghi chú</label>
                    <el-form-item prop="height" label-width="0" ><br/>
                      <el-input type="textarea" v-model="addIpAddress.note" style="with:100%" :maxlength="500" show-word-limit></el-input>                  
                    </el-form-item> 
                    <el-button class="search" @click="handleAddIP" type="primary">Thêm mới</el-button>
                  </el-form>
                </el-col>
              </el-row>  
            </el-tab-pane>          


          </el-tabs>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="changeObjectVisiable = false">Bỏ qua</el-button>
          <el-button type="primary" @click="updateObjectInfo()">Đồng ý</el-button>
        </div>  
    </el-dialog>     
    

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
import waves from "@/directive/waves"; // Waves directive
import Resource from "@/api/resource";
import { mapGetters } from 'vuex';
import { validString } from "@/utils";
import ImageCropper from '@/components/ImageCropper';

const objectUndefineResource = new Resource("contact/Object/search");
const countriesResource = new Resource("ais/country");
const addObjectResource = new Resource("contact/Object/addObjectUnInfo");
const editObjectResource = new Resource("contact/Object/editObjectUnInfo");
const deleteObjectResource = new Resource("contact/Object/delObjectUnInfo");
const ObjectDetailResource = new Resource("contact/Object/detail/getDetailObjectInfo");
const addIpResource = new Resource("contact/Object/addIpObjectInfo"); 
const deleteIPObjectResource = new Resource("contact/Object/delIpObjectInfo")
export default {
  components: {
    Pagination,
    ImageCropper
  },
  directives: { waves },
  data() {
    return {
      changeObjectVisiable:false,
      addObjectVisiable : false,
      activeNameFormEdit:'first',
      value: [1],
      totalSDT:0,
      totalIP:0,
      ips:null,
      phones : null,
      multiple: true,
      search: true,
      controls: true,
      isOpen: false,
      list_country:null,
      activeNames: '0',

      typeStatusInfo:{
        value : "", 
        id : 0,
      },
      typeStatusValue:[
          {id : 0 , value :'Không sử dụng'},
          {id : 1 , value :'Sử dụng'},
      ],

      addIpAddress:{
        uuid :0,
        ip_address :'',
        note :''
      },
      deleteIp:{
        uuid :0,
        ip_address :'',
      },

      addObject: {
        uuid:'',
        country_id:'',
        chieudai:0,
        chieurong:0,
        name:'',
        imagePath:'',
        sourceIP:'',
        sourcePort:0,
        destIP:'',
        destPort:0,
        userUpdate:''
      },

      editObject: {
        uuid:'',
        country_id:'',
        chieudai:0,
        chieurong:0,
        name:'',
        imagePath:'',
        sourceIP:'',
        sourcePort:0,
        destIP:'',
        destPort:0,
        userUpdate:''
      },

      countryInfo:{
        name : "", 
        id : 0,
      },

      list: null,
      total: 0,
      loading: true,
      query: {
          currentPage: 1,
          rowsPerPage: 10,
          sort: "createdAt",
          keyword: "",
      },

      rules: {
      },
      imagecropperShow: false,
      imagecropperKey: 0,
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
  },
  methods: {
    async getRoleMenuPage(){
      console.log(this.$store.getters.roleMenuActions);
      for(var i=0; i<this.$store.getters.roleMenuActions.length;i++){
        if(this.$store.getters.roleMenuActions[i].pageUrl == "/contact/object-undefine"){
          this.checkCreateRole = this.$store.getters.roleMenuActions[i].canCreate;
          this.checkEditRole = this.$store.getters.roleMenuActions[i].canUpdate;
          this.checkViewRole = this.$store.getters.roleMenuActions[i].canRead;
          this.checkDeleteRole = this.$store.getters.roleMenuActions[i].canDelete;
        }
      }
    },

    async getList() {
      this.loading = true;
      this.query.keyword = this.query.keyword.trim();
      const { data, total } = await objectUndefineResource.listByPost(this.query);
      this.list = data;

      countriesResource.list().then((response) => {
            if (response.status === 200) {
              this.list_country = response.data;
              this.countryInfo = {
                id : response.data[0].id,
                name : response.data[0].name              
              };
              
            }
          })
          .catch((err) => {
            console.log(err);
      }); 
      
      if (data) {
        this.total = total;
      }
      this.loading = false;
    }, 


    handleChangeObject(data) {
        if(data) { // Fill data into model for update row
           ObjectDetailResource.listByPost({uuid:data.uuid}).then((response) => {             
              if (response.status === 200) {
                 this.editObject = {
                     uuid : response.data.objectUnInfoDto.uuid,
                     country_id : response.data.objectUnInfoDto.country_id,
                     name : response.data.objectUnInfoDto.name,
                     chieudai : response.data.objectUnInfoDto.chieudai,
                     chieurong : response.data.objectUnInfoDto.chieurong,
                     imagePath :  response.data.objectUnInfoDto.imagePath,
                     sourceIP: response.data.objectUnInfoDto.sourceIP,
                     sourcePort: response.data.objectUnInfoDto.sourcePort,
                     destIP: response.data.objectUnInfoDto.destIP,
                     destPort: response.data.objectUnInfoDto.destPort,
                     userUpdate: response.data.objectUnInfoDto.userUpdate,
                 }; 
                 this.countryInfo = {
                     id : response.data.objectUnInfoDto.country_id,
                     name : response.data.objectUnInfoDto.countryName
                 };

                 this.addIpAddress.uuid =response.data.objectUnInfoDto.uuid;
                 this.ips = response.data.ips;

              }
            })
            .catch((err) => {
              console.log(err);
            });                    
        }

        this.changeObjectVisiable = true; 
       
    },
    updateObjectInfo(){
      this.$refs.ObjectUndefineEditForm.validate((valid) => {
        if (valid) {  
              if(!this.validateIP(this.editObject.sourceIP)){
                  this.$notify({
                      title: 'Có lỗi xảy ra',
                      message: "IP nguồn không đúng định dạng",
                      type: 'error'
                  });              
              }else if(!this.validateIP(this.editObject.destIP)){
                  this.$notify({
                      title: 'Có lỗi xảy ra',
                      message: "IP đích không đúng định dạng",
                      type: 'error'
                  });   
              }else{
                console.log("mrd updateObjectInfo()");
                this.editObject.country_id = this.countryInfo.id;  
                editObjectResource.store(this.editObject).then((response) => {              
                    this.query.currentPage = 1;
                    this.getList();
                    if (response.status === 200 && response.data != null) {
                        this.changeObjectVisiable = false;
                        this.$notify({
                        title: 'Thành công',
                        message: 'Sửa thành công',
                        type: 'success'
                      });
                    
                    }else
                      this.$notify({
                          title: 'Có lỗi xảy ra',
                          message: response.message,
                          type: 'error'
                      });
                  })
                  .catch((err) => {
                    console.log(err);
                  }); 
              }
        }
      });
    },

    handleAddObject(val) {
        console.log(val);
        this.addObjectVisiable = true;
       
    },
    async addObjectInfo(){
        this.$refs.ObjectUndefineAddForm.validate((valid) => {
            if (valid) {    
              if(!this.validateIP(this.addObject.sourceIP)){
                  this.$notify({
                      title: 'Có lỗi xảy ra',
                      message: "IP nguồn không đúng định dạng",
                      type: 'error'
                  });              
              }else if(!this.validateIP(this.addObject.destIP)){
                  this.$notify({
                      title: 'Có lỗi xảy ra',
                      message: "IP đích không đúng định dạng",
                      type: 'error'
                  });   
              }else{
                  console.log("mrd addObjectInfo()");
                  this.addObject.country_id = this.countryInfo.id;
                  addObjectResource.store(this.addObject).then((response) => {
                      if (response.status === 200 && response.data != null) {
                          this.getList();      
                          this.addObjectVisiable = false;
                          this.$notify({
                            title: 'Thành công',
                            message: 'Thêm thành công',
                            type: 'success'
                          });
                          
                      }else{
                          this.$notify({
                            title: 'Có lỗi xảy ra',
                            message: response.message,
                            type: 'error'
                          });

                      }
                      })
                      .catch((err) => {
                      console.log(err);
                      }); 
              }
              

            
            }
            this.getList();
        });
    },
    handleFilter(){
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

    handleClick(tab, event) {
        console.log(tab, event);
    },
    handleChangeTypeStatus(){
        console.log("handleChangeTypeStatus : ");
    },
    changeCountry(){
        console.log("changeCountryId : " + this.countryInfo.id);

    },

    validateIP(ipAddress){    
      if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipAddress))
      {
        return true;
      }else{
        return false;
      }

    },

    getIPByMMSI(){
      console.log("getIPByMMSI");
    },
    getSDTByMMSI(){
      console.log("getSDTByMMSI");
    },

    reloadDetailObj(val){
           ObjectDetailResource.listByPost({uuid:val}).then((response) => {             
              if (response.status === 200) {
                 this.ips = response.data.ips;

              }
            })
            .catch((err) => {
              console.log(err);
            });    
    },   

    handleAddIP(){     
      console.log("handleAddIP");
      if(this.addIpAddress.ip_address != null && this.addIpAddress.ip_address.trim() != "" ){
        if(this.validateIP(this.addIpAddress.ip_address)){
          this.$refs.addIpAddressForm.validate((valid) => {
            if (valid) {                 
            addIpResource.store(this.addIpAddress).then((response) => {
                if (response.status === 200 && response.data.data != null) {
                    this.$notify({
                          title: "Thành Công",
                          message: "Thêm IP thành công!",
                          type: "success",
                    });
                    this.addIpAddress.ip_address = '';
                    this.addIpAddress.note = '';
                    this.getList();
                    this.reloadDetailObj(this.addIpAddress.uuid);
                }else
                    this.$notify({
                      title: "Có lỗi xảy ra",
                      message: response.data.message,
                      type: "error",
                    });                  
                })
                .catch((err) => {
                console.log(err);
                }); 
            
            }

          });
        }else{
            this.$notify({
              title: "Waring",
              message: "Địa chỉ IP không đúng định dạng",
              type: "warning",
            });
        }

      }else{
            this.$notify({
              title: "Waring",
              message: "Địa chỉ IP không được để trống",
              type: "warning",
            });
      }

    },

    handleDeleteObject(id){
        console.log("handleDeleteObject");
        this.$confirm('Bạn có chắc chắn xóa', 'Cảnh báo', {
          confirmButtonText: 'Đồng ý',
          cancelButtonText: 'Hủy bỏ',
          type: 'warning',
        })
        .then(() => {
          deleteObjectResource.destroy(id)
          .then(response => {
            if (response.status === 200) {
              this.$notify({
                title: 'Thành công',
                message: 'Xóa thành công',
                type: 'success'
              });
            }
            this.getList();
          })
          .catch(err => {
            console.log(err);
          })
        })
        .catch(() => {
          console.log('cancel');
        });

    },
    handleDeleteIP(ip){
      console.log("handleDeleteIP : " + ip);
      this.deleteIp.uuid = this.editObject.uuid;
      this.deleteIp.ip_address = ip;

      console.log(" this.deleteIp.id : " +  this.deleteIp.uuid);
       this.$confirm('Bạn có chắc chắn xóa', 'Cảnh báo', {
          confirmButtonText: 'Đồng ý',
          cancelButtonText: 'Hủy bỏ',
          type: 'warning',
        })
        .then(() => {
          deleteIPObjectResource.listByPost(this.deleteIp)
          .then(response => {
            if (response.status === 200) {
              this.$notify({
                title: 'Thành công',
                message: 'Xóa thành công',
                type: 'success'
              });
            }
            this.getList();
            this.reloadDetailObject(this.deleteIp.uuid);
          })
          .catch(err => {
            console.log(err);
          })
        })
        .catch(() => {
          console.log('cancel');
        });
    },

    reloadDetailObject(val){
           ObjectDetailResource.listByPost({uuid:val}).then((response) => {             
              if (response.status === 200) {
                 this.ips = response.data.ips;
              }
            })
            .catch((err) => {
              console.log(err);
            });    
    },    

    closeChangeAvatar() {
          this.imagecropperShow = false;
    },



  }
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

.el-input-number{
  width: 100%;
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
.el-form-item--medium .el-form-item__content, .el-form-item--medium .el-form-item__label{
  line-height: 15px;
}
.el-textarea__inner {
    height: 200px;
}

</style>
