<template>
  <div class="app-container">
    <h2 class="no-margin-top">Danh bạ bờ</h2>
    <div class="filter-container">
      <el-row :gutter="20" >
        <div style="float:right">
            <template>            
              <div class="block">
                <el-input v-model="query.keyword" placeholder="Tìm kiếm (Tên, quốc tịch...)" style="width :400px"></el-input>
              </div>    
              <el-button class="search" @click="handleFilter()" type="primary">Tìm kiếm</el-button>
              <el-button v-show="checkCreateRole" class="search" @click="handleAddLand(null)" type="primary">Thêm mới</el-button>         
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
      <el-table-column label="ID" width="100">
          <template slot-scope="scope">
               <span>{{ scope.row.id }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Tên">
          <template slot-scope="scope">
               <span>{{ scope.row.name }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Quốc tịch" width="150">
          <template slot-scope="scope">
               <span>{{ scope.row.countryName }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Kinh độ" width="85">
          <template slot-scope="scope">
               <span>{{ scope.row.longitude }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Vĩ độ" width="85">
          <template slot-scope="scope">
               <span>{{ scope.row.latitude }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Ghi chú" width="350"> 
          <template slot-scope="scope">
               <span>{{ scope.row.description }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Trạng thái" width="130">
          <template slot-scope="scope" >
               <span v-if="scope.row.status == 1">Sử dụng</span>
               <span v-else-if="scope.row.status == 0">Không sử dụng</span>
               <span v-else-if="scope.row.status == null"></span>
          </template>
      </el-table-column>

      <el-table-column label="Tác động" width="140">
          <template slot-scope="scope">      
            <el-button v-show="checkEditRole" type="primary" size="small" @click="handleChangeLand(scope.row)">Sửa</el-button>
            <el-button v-show="checkDeleteRole" type="danger" size="small" @click="handleDeleteLand(scope.row.id)">Xóa</el-button>
          </template>
      </el-table-column>

    </el-table>

      <!-- form add Land -->
      <el-dialog top="5vh"  title="Thông tin" :visible.sync="addLandVisiable" width="70%" :close-on-click-modal="false" >
        <el-form ref="addLandForm" :model="addLand" :rules="rules" label-position="left" label-width="180px" class="frmAddLand">
            <el-row>
                <el-col :span="12" style="padding:5px">
                  <label>Tên</label>
                  <el-form-item prop="name" label-width="0"><br/>
                    <el-input placeholder="Tên" v-model="addLand.name" :maxlength="100" show-word-limit></el-input>
                  </el-form-item> 
                  <label>Quốc tịch</label>
                  <el-form-item label="" prop="country" label-width="0" ><br/>
                    <el-select  v-model="addLand.country" placeholder="Chọn quốc tịch"  @change="changeCountry()" style="width :100%">
                        <el-option v-for="item in list_country" :key="item.id" :label="item.name" :value="item.id" >                         
                        </el-option>
                    </el-select>
                  </el-form-item>
                  <label>Trạng thái</label>
                  <el-form-item label="" prop="status" label-width="0" ><br/>
                    <el-select v-model="addLand.status" value-key="value" placeholder="Trạng thái" @change="handleChangeTypeStatus" style="width :100%" > 
                      <el-option v-for="item in typeStatusValue" :key="item.id" :label="item.value" :value="item.id"></el-option>
                    </el-select>
                  </el-form-item>  
                  <el-row>
                      <el-col :span="12" style="padding-right:3px">
                        <label>Vĩ độ</label>
                        <el-form-item label="" prop="latitude" label-width="0" ><br/>
                            <el-input-number v-model="addLand.latitude"></el-input-number>
                        </el-form-item>                         
                      </el-col>
                      <el-col :span="12" style="padding-left:3px">
                        <label>Kinh độ</label> 
                        <el-form-item label="" prop="longitude" label-width="0" ><br/>
                            <el-input-number v-model="addLand.longitude"></el-input-number>
                        </el-form-item>    
                      </el-col>
                  </el-row>     
                </el-col>

                <el-col :span="12" style="padding:5px">
                  <label>Vai trò</label>
                  <el-form-item prop="vai_tro" label-width="0"><br/>
                    <el-input placeholder="Vai trò" v-model="addLand.vai_tro" :maxlength="100" show-word-limit></el-input>
                  </el-form-item> 
                  <label>Chức năng</label>
                  <el-form-item label="" prop="chuc_nang" label-width="0" ><br/>
                    <el-input placeholder="Chức năng" v-model="addLand.chuc_nang" :maxlength="100" show-word-limit></el-input>
                  </el-form-item>
                  <label>Tổ chức</label>
                  <el-form-item label="" prop="to_chuc" label-width="0" ><br/>
                    <el-input placeholder="Tổ chức" v-model="addLand.to_chuc" :maxlength="100" show-word-limit></el-input>
                  </el-form-item>  
                   <label>Ghi chú</label>
                  <el-form-item label="" prop="note" label-width="0" ><br/>
                    <el-input type="textarea" placeholder="Ghi chú" v-model="addLand.note" :maxlength="500" show-word-limit></el-input>
                  </el-form-item>  
                </el-col>
            </el-row>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addLandVisiable = false">Bỏ qua</el-button>
          <el-button type="primary" @click="addLandInfo()">Đồng ý</el-button>
        </div>  
    </el-dialog>

      <!-- form edit Land -->
      <el-dialog top="5vh"  title="Thông tin" :visible.sync="changeLandVisiable" width="70%" :close-on-click-modal="false" >
                        
          <el-tabs v-model="activeNameFormEdit" @tab-click="handleClick">               
            <el-tab-pane label="Thông tin chung" name="first">                
              <el-row > 
                <el-form ref="editLandForm" :model="editLand" :rules="rules" label-position="left" label-width="180px" class="frmEditLand">
                    <el-col :span="12" style="padding:5px">
                        <label>Tên</label>
                        <el-form-item prop="name" label-width="0"><br/>
                          <el-input placeholder="Tên" v-model="editLand.name"></el-input>
                        </el-form-item> 
                        <label>Quốc tịch</label>
                        <el-form-item label="" prop="country" label-width="0" ><br/>
                          <el-select  v-model="countryInfo.id" placeholder="Chọn quốc tịch"  @change="changeCountry()" style="width :100%">
                              <el-option v-for="item in list_country" :key="item.id" :label="item.name" :value="item.id" >                          
                              </el-option>
                          </el-select>
                        </el-form-item>
                        <label>Trạng thái</label>
                        <el-form-item label="" prop="status" label-width="0" ><br/>
                          <el-select v-model="editLand.status" value-key="value" placeholder="Trạng thái" @change="handleChangeTypeStatus" style="width :100%" > 
                            <el-option v-for="item in typeStatusValue" :key="item.id" :label="item.value" :value="item.id"></el-option>
                          </el-select>
                        </el-form-item>  
                        <el-row>
                            <el-col :span="12" style="padding-right:3px">
                              <label>Vĩ độ</label>
                              <el-form-item label="" prop="latitude" label-width="0" ><br/>
                                  <el-input-number v-model="editLand.latitude"></el-input-number>
                              </el-form-item>                         
                            </el-col>
                            <el-col :span="12" style="padding-left:3px">
                              <label>Kinh độ</label> 
                              <el-form-item label="" prop="longitude" label-width="0" ><br/>
                                  <el-input-number v-model="editLand.longitude"></el-input-number>
                              </el-form-item>    
                            </el-col>
                        </el-row> 

                        <label>Cập nhật bởi</label>
                        <el-form-item label="" prop="userName" label-width="0" ><br/>
                              <el-input :disabled=true v-model="editLand.username"></el-input>
                        </el-form-item> 

                        <label>Thời gian cập nhật</label>
                        <el-form-item label="" prop="updateTime" label-width="0" ><br/>
                          <el-input :disabled=true v-model="editLand.updateTime"></el-input>
                        </el-form-item> 

                    </el-col>

                  <el-col :span="12" style="padding:5px">
                    <label>Vai trò</label>
                    <el-form-item prop="vai_tro" label-width="0"><br/>
                      <el-input placeholder="Vai trò" v-model="editLand.vai_tro"></el-input>
                    </el-form-item> 
                    <label>Chức năng</label>
                    <el-form-item label="" prop="chuc_nang" label-width="0" ><br/>
                      <el-input placeholder="Chức năng" v-model="editLand.chuc_nang"></el-input>
                    </el-form-item>
                    <label>Tổ chức</label>
                    <el-form-item label="" prop="to_chuc" label-width="0" ><br/>
                      <el-input placeholder="Tổ chức" v-model="editLand.to_chuc"></el-input>
                    </el-form-item>  
                    <label>Ghi chú</label>
                    <el-form-item label="" prop="note" label-width="0" ><br/>
                      <el-input type="textarea" placeholder="Ghi chú" :maxlength="500" show-word-limit v-model="editLand.note"></el-input>
                    </el-form-item>  
                      <div slot="footer" class="dialog-footer">
                          <el-button @click="changeLandVisiable = false">Bỏ qua</el-button>
                          <el-button type="primary" @click="updateLandInfo()">Đồng ý</el-button>
                      </div>  
                  </el-col>
                 </el-form>
              </el-row>              
            </el-tab-pane>              
            
            <el-tab-pane label="Danh mục IP" name="second">
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

            <el-tab-pane label="Danh mục SĐT vệ tinh" name="six">
              <el-row>  
                <el-col :span="15" style="padding:5px">
                  <el-table
                    v-loading="loading"
                    :data="phones"
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
                    <el-table-column label="SĐT vệ tinh" width="200">
                      <template slot-scope="scope">
                        <span>{{ scope.row.phoneNumber }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="Ghi chú">
                      <template slot-scope="scope">
                        <span>{{ scope.row.note }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="Tác vụ" align="center" width="80">
                      <template slot-scope="scope">                           
                        <el-button type="danger" size="small" @click="handleDeletePhone(scope.row.phoneNumber)">Xóa</el-button>
                      </template>
                    </el-table-column>
                </el-table>
                <pagination
                  v-show="totalSDT > 10"
                  :total="totalSDT"
                  :page.sync="query.currentPage"
                  :limit.sync="query.rowsPerPage"
                  @pagination="getSDTByMMSI"
                />
                </el-col>

                <el-col :span="9" style="padding:5px">
                  <el-form ref="addPhoneForm" :model="addPhone" :rules="rules" label-position="left" label-width="180px" class="frmAddPhone">
                  <label>Số điện thoại</label>
                  <el-form-item prop="imo" label-width="0" ><br/>
                     <el-input v-model="addPhone.phoneNumber" style="with:100%"></el-input>                  
                  </el-form-item> 
                  <label>Ghi chú</label>
                  <el-form-item prop="height" label-width="0" ><br/>
                    <el-input type="textarea" v-model="addPhone.note" style="with:100%" :maxlength="500" show-word-limit></el-input>                  
                  </el-form-item> 
                  <el-button class="search" @click="handleAddSDT" type="primary">Thêm mới</el-button>
                  </el-form>
                </el-col>
              </el-row>  
            </el-tab-pane>

          </el-tabs>

        <div slot="footer" class="dialog-footer">
          <el-button @click="changeLandVisiable = false">Bỏ qua</el-button>
          <el-button type="primary" @click="updateLandInfo()">Đồng ý</el-button>
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

const landResource = new Resource("contact/land/search");
const countriesResource = new Resource("ais/country");
const addLandResource = new Resource("contact/land/addHeadquater");
const editLandResource = new Resource("contact/land/updateHeadquater");
const deleteLandResource = new Resource("contact/land/deleteHeadquater");
const landDetailResource = new Resource("contact/land/detail/getDetailHeadquarter");
const addIpResource = new Resource("contact/land/addIpHeadQuarter"); 
const deleteIPLandResource = new Resource("contact/land/delIpHeadQuarter");
const addPhoneResource = new Resource("contact/land/addPhoneHeadQuarter"); 
const deletePhoneLandResource = new Resource("contact/land/delPhoneHeadQuarter");

export default {
  components: {
    Pagination,
    //AdvancedSelect ,
  },
  directives: { waves },
  data() {
    return {
      changeLandVisiable:false,
      addLandVisiable : false,
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
        value : "Chọn trạng thái", 
        id : null,
      },
      typeStatusValue:[
          {id : 0 , value :'Không sử dụng'},
          {id : 1 , value :'Sử dụng'},
      ],
      addIpAddress:{
        id :0,
        ip_address :'',
        note :''
      },
      deleteIp:{
        id :0,
        ip_address :'',
      },
      addPhone:{
        id :0,
        phoneNumber :'',
        note :''
      },
      deletePhone:{
        id :0,
        phoneNumber :'',
      },
      addLand: {
        name:'',
        country : null,               //id
        status :null ,
        latitude :0 ,              //vĩ độ
        longitude : 0,             // kinh độ
        chuc_nang : null,
        to_chuc : null,
        vai_tro : null,
        note : '',                  //ghi chú
      },
      editLand: {
        id:0,
        name:'',
        country : 0,               //id
        status :0 ,
        latitude :0 ,              //vĩ độ
        longitude : 0,             // kinh độ
        chuc_nang : null,
        to_chuc : null,
        vai_tro : null,
        note : '',                  //ghi chú\
        username : '',
        updateTime :''
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
        name : [{ required: true, message: 'Tên bắt buộc nhập', trigger: 'blur' }],
        status : [{ required: true, message: 'Cần phải chọn trạng thái', trigger: 'blur' }],
        country : [{ required: true, message: 'Quốc tịch bắt buộc nhập', trigger: 'blur' }],
      },
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
    this.getList();
    this.getRoleMenuPage();
  },
  methods: {
    async getRoleMenuPage(){
      console.log(this.$store.getters.roleMenuActions);
      for(var i=0; i<this.$store.getters.roleMenuActions.length;i++){
        if(this.$store.getters.roleMenuActions[i].pageUrl == "/contact/organization"){
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
      const { data, total } = await landResource.listByPost(this.query);
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
    
    validateIP(ipAddress){    
      if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipAddress))
      {
        return true;
      }else{
        return false;
      }

    },   
    reloadDetailLand(val){
           landDetailResource.listByPost({id:val}).then((response) => {             
              if (response.status === 200) {
                 this.ips = response.data.ips;
                 this.phones = response.data.phones;

              }
            })
            .catch((err) => {
              console.log(err);
            });    
    },

    handleChangeLand(data) {      
        if(data) { // Fill data into model for update row
           landDetailResource.listByPost({id:data.id}).then((response) => {             
              if (response.status === 200) {
                 this.editLand = {
                     id : response.data.headQuarterDto.id,
                     name : response.data.headQuarterDto.name,
                     country : response.data.headQuarterDto.countryId,
                     status : response.data.headQuarterDto.status,
                     latitude : response.data.headQuarterDto.latitude,
                     longitude : response.data.headQuarterDto.longitude,
                     chuc_nang : response.data.headQuarterDto.chucNang,
                     to_chuc : response.data.headQuarterDto.to_chuc,
                     vai_tro : response.data.headQuarterDto.vaiTro,
                     note : response.data.headQuarterDto.description,
                     username: response.data.headQuarterDto.username,
                     updateTime: response.data.headQuarterDto.updatedTime,
                 }; 
                 if(this.editLand.updateTime != '' ){
                   this.editLand.updateTime = this.editLand.updateTime.substring(0, 19)
                 }
                 this.countryInfo = {
                     id : response.data.headQuarterDto.countryId,
                     name : response.data.headQuarterDto.countryName
                 };
                 this.addIpAddress.id =response.data.headQuarterDto.id;
                 this.addPhone.id = response.data.headQuarterDto.id;
                 this.ips = response.data.ips;
                 this.phones = response.data.phones;
                 this.addPhone.phoneNumber = null;
                 this.addIpAddress.ip_address = null;
                 this.addPhone.note = '';
                 this.addIpAddress.note = '';

              }
            })
            .catch((err) => {
              console.log(err);
            });                    
        }

        this.changeLandVisiable = true; 
       
    },
    updateLandInfo(){
        //check kinh độ vĩ độ
        if(-180 > this.editLand.longitude || 180 < this.editLand.longitude){
          this.$notify({
              title: "Có lỗi xảy ra",
              message: "Kinh độ phải nằm trong [-180,180]",
              type: "error",
          }); 
        }else if( -90 > this.editLand.latitude || 90 < this.editLand.latitude){
          this.$notify({
              title: "Có lỗi xảy ra",
              message: "Vĩ độ phải nằm trong [-90,90]",
              type: "error",
          }); 
        }else{      
          this.$refs.editLandForm.validate((valid) => {
            if (valid) {
              console.log("mrd updateLandInfo()");
              this.editLand.username = this.$store.getters.userName;
                      
              editLandResource.store(this.editLand).then((response) => {                  
                  this.query.currentPage = 1;
                  this.getList();
                  if (response.status === 200 && response.data != null) {
                      this.$notify({
                      title: 'Thành công',
                      message: 'Sửa thành công',
                      type: 'success'
                    });
                    this.changeLandVisiable = false;
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
          });
        }
    },

    handleAddLand(val) {
        console.log(val);
        this.addLand.name = '';
        this.addLand.vai_tro = '';
        this.addLand.chuc_nang = '';
        this.addLand.to_chuc = '';
        this.addLand.note = '';
        this.addLand.longitude = 0;
        this.addLand.latitude = 0;
        this.addLandVisiable = true;
        this.addLand.status = null;
       
    },
    async addLandInfo(){
        //check kinh độ vĩ độ
        if(-180 > this.addLand.longitude || 180 < this.addLand.longitude){
          this.$notify({
              title: "Có lỗi xảy ra",
              message: "Kinh độ phải nằm trong [-180,180]",
              type: "error",
          }); 
        }else if( -90 > this.addLand.latitude || 90 < this.addLand.latitude){
          this.$notify({
              title: "Có lỗi xảy ra",
              message: "Vĩ độ phải nằm trong [-90,90]",
              type: "error",
          }); 
        }else{
            this.$refs.addLandForm.validate((valid) => {
                if (valid) {                 
                console.log("mrd addLandInfo()");
                addLandResource.store(this.addLand).then((response) => {
                    if (response.status === 200 && response.data != null) {
                        this.$notify({
                              title: "Thành Công",
                              message: "Thêm thành công!",
                              type: "success",
                        });
                        this.addLandVisiable = false;
                        this.getList();
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
                
            });
        }
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
    getIPByMMSI(){
      console.log("getIPByMMSI");
    },
    getSDTByMMSI(){
      console.log("getSDTByMMSI");
    },

    handleAddIP(){     
      console.log("handleAddIP");
      if(this.addIpAddress.ip_address != null && this.addIpAddress.ip_address.trim() != "" ){
        if(this.validateIP(this.addIpAddress.ip_address)){
          this.$refs.addIpAddressForm.validate((valid) => {
            if (valid) {                 
            addIpResource.store(this.addIpAddress).then((response) => {
                if (response.status === 200 && response.data != null) {
                    this.$notify({
                          title: "Thành Công",
                          message: "Thêm IP thành công!",
                          type: "success",
                    });
                    this.addIpAddress.ip_address = '';
                    this.addIpAddress.note = '';
                    this.getList();
                    this.reloadDetailLand(this.addIpAddress.id);
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
    handleAddSDT(){
      console.log("handleAddSDT");
      if(this.addPhone.phoneNumber != null && this.addPhone.phoneNumber.trim() != ""){
        this.$refs.addPhoneForm.validate((valid) => {
            if (valid) {                 
            addPhoneResource.store(this.addPhone).then((response) => {
                if (response.status === 200 && response.data != null) {
                    this.$notify({
                      title: 'Thành công',
                      message: 'Thêm Số điện thoại thành công',
                      type: 'success'
                    });
                    this.addPhone.phoneNumber = '';
                    this.addPhone.note = '';
                    this.getList();
                    this.reloadDetailLand(this.addPhone.id);
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
      }else{
            this.$notify({
              title: "Waring",
              message: "Số điện thoại không được để trống",
              type: "warning",
            });
      }

    },

    handleDeleteLand(id){
        console.log("handleDeleteLand");
        this.$confirm('Bạn có chắc chắn xóa', 'Cảnh báo', {
          confirmButtonText: 'Đồng ý',
          cancelButtonText: 'Hủy bỏ',
          type: 'warning',
        })
        .then(() => {
          deleteLandResource.destroy(id)
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
      this.deleteIp.id = this.editLand.id;
      this.deleteIp.ip_address = ip;

      console.log(" this.deleteIp.id : " +  this.deleteIp.id);
       this.$confirm('Bạn có chắc chắn xóa', 'Cảnh báo', {
          confirmButtonText: 'Đồng ý',
          cancelButtonText: 'Hủy bỏ',
          type: 'warning',
        })
        .then(() => {
          deleteIPLandResource.listByPost(this.deleteIp)
          .then(response => {
            if (response.status === 200) {
              this.$notify({
                title: 'Thành công',
                message: 'Xóa thành công',
                type: 'success'
              });
            }
            this.getList();
            this.reloadDetailLand(this.deleteIp.id);
          })
          .catch(err => {
            console.log(err);
          })
        })
        .catch(() => {
          console.log('cancel');
        });
    },

    handleDeletePhone(phoneNumber){
      console.log("handleDeletePhone : " + phoneNumber);
      this.deletePhone.id = this.editLand.id;
      this.deletePhone.phoneNumber = phoneNumber;

      console.log(" this.deletePhone.id : " +  this.deletePhone.id);
       this.$confirm('Bạn có chắc chắn xóa', 'Cảnh báo', {
          confirmButtonText: 'Đồng ý',
          cancelButtonText: 'Hủy bỏ',
          type: 'warning',
        })
        .then(() => {
          deletePhoneLandResource.listByPost(this.deletePhone)
          .then(response => {
            if (response.status === 200) {
              this.$notify({
                title: 'Thành công',
                message: 'Xóa Số điện thoại thành công',
                type: 'success'
              });
            }
            this.getList();
            this.reloadDetailLand(this.deletePhone.id);
          })
          .catch(err => {
            console.log(err);
          })
        })
        .catch(() => {
          console.log('cancel');
        });
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
