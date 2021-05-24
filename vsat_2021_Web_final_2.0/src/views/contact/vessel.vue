<template>
  <div class="app-container">
    <h2 class="no-margin-top">Danh bạ tàu</h2>
    <div class="filter-container">
      <el-row :gutter="20" >
        <div style="float:right">
            <template>            
              <div class="block">
                <el-input v-model="query.vesselName" placeholder="Tên tàu" :maxlength="100" style="width :200px"></el-input>
              </div>
              <div class="block">
                <el-input v-model="query.mmsi" placeholder="MMSI" style="width :120px"></el-input>
              </div>
              <div class="block">
                <el-input v-model="query.ip" placeholder="IP" style="width :120px"></el-input>
              </div>
              <div class="block">                 
                <el-select  v-model="query.countryId"  filterable clearable placeholder="Chọn quốc tịch"  @change="changeCountrySearch()" >
                    <el-option v-for="item in list_countrySearch" :key="item.id" :label="item.name" :value="item.id" >                          
                    </el-option>
                 </el-select>
              </div>
              <div class="block">
                <el-select v-model="query.vesselTypeId"  filterable clearable placeholder="Loại tàu" @change="changeVesselTypeSearch()">
                  <el-option v-for="item in list_vesselTypeSearch" :key="item.id" :label="item.typeName" :value="item.id"></el-option>
                </el-select>
              </div>
          </template>
        </div>
      </el-row>
      <el-row :gutter="20" style="margin-top:15px;"> 
        <div style="float:right">
          <el-button class="search" v-model="activeNames"  @click="isOpen = !isOpen" type="primary">Tìm kiếm nâng cao</el-button> 
          <el-button class="search" @click="handleFilter()" type="primary">Tìm kiếm</el-button>
          <el-button v-show="checkCreateRole" class="search" @click="handleAddVessel(null)" type="primary">Thêm mới</el-button>
        </div>
      </el-row>

       <el-row v-show="isOpen" style="margin-top:15px;background:linear-gradient(to bottom right, #8ac3f3 0%, #0c518c 100%);border-radius:5px;">      
          <div >           
                <el-row > 
                  <el-row>
                      <template>
                        <div style="float:right;padding-top: 7px;padding-bottom:3px">
                          <div class="block">
                            <el-input v-model="query.advancePhone" placeholder="Số điện thoại"></el-input>
                          </div>
                          <div class="block">
                            <el-input v-model="query.advanceMmsiMin" placeholder="MMSI nhỏ nhất"></el-input>
                          </div>
                          <div class="block">
                            <el-input v-model="query.advanceHeightMin" placeholder="Chiều rộng nhỏ nhất"></el-input>
                          </div>
                          <div class="block">
                            <el-input v-model="query.advanceIMOMin" placeholder="IMO nhỏ nhất"></el-input>
                          </div>
                          <div class="block">
                            <el-input v-model="query.advanceWidthMin" placeholder="Chiều dài nhỏ nhất"></el-input>
                          </div>                     
                        </div>
                    </template>
                  </el-row>
                  <el-row>
                      <template>
                        <div style="float:right; padding-bottom: 7px;">
                          <div class="block">
                            <el-input v-model="query.advancecallSign" placeholder="Hô hiệu"></el-input>
                          </div>
                          <div class="block">
                            <el-input v-model="query.advanceMmsiMax" placeholder="MMSI lớn nhất"></el-input>
                          </div>
                          <div class="block">
                            <el-input v-model="query.advanceHeightMax" placeholder="Chiều rộng lớn nhất"></el-input>
                          </div>
                          <div class="block">
                            <el-input v-model="query.advanceIMOMax" placeholder="IMO lớn nhất"></el-input>
                          </div>
                          <div class="block">
                            <el-input v-model="query.advanceWidthMax" placeholder="Chiều dài lớn nhất"></el-input>
                          </div>
                        </div>
                    </template>
                  </el-row>
                </el-row>
              
            
          </div>
      </el-row>
      <!-- mrd -->
    
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
      <el-table-column label="MMSI" width="100">
          <template slot-scope="scope">
               <span>{{ scope.row.mmsi }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Tên tàu">
          <template slot-scope="scope">
               <span>{{ scope.row.vesselName }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Quốc tịch" width="250">
          <template slot-scope="scope">
               <span>{{ scope.row.countryName }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Loại tàu" width="170">
          <template slot-scope="scope">
               <span>{{ scope.row.vesselTypeName }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Hô hiệu" width="200">
          <template slot-scope="scope">
               <span>{{ scope.row.callSign }}</span>
          </template>
      </el-table-column>

      <el-table-column label="IMO" width="100"> 
          <template slot-scope="scope">
               <span>{{ scope.row.imo }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Kích thước" width="100">
          <template slot-scope="scope" >
               <span>{{ scope.row.width }}</span>
          </template>
      </el-table-column>

      <el-table-column label="Tác động" width="220">
          <template slot-scope="scope">      
            <router-link :to="'/contact/vessel-detail?mmsi=' + scope.row.mmsi">
              <el-button v-show="checkViewRole" type="primary" size="small" style="margin-right: 8px;">Chi tiết</el-button>
            </router-link>       

            <el-button v-show="checkEditRole" type="primary" size="small" @click="handleChangeVessel(scope.row)">Sửa</el-button>
            <el-button v-show="checkDeleteRole" type="danger" size="small" @click="handleDeleteVessel(scope.row.mmsi)">Xóa</el-button>
          </template>
      </el-table-column>

    </el-table>

      <!-- form add Vessel -->
      <el-dialog top="5vh"  title="Thông tin" :visible.sync="addVesselVisiable" width="70%" :close-on-click-modal="false" >
        <el-form ref="addVesselForm" :model="addVessel" :rules="rules" label-position="left" label-width="180px" class="frmEditVessel">
          <el-tabs v-model="activeNameFormEdit" @tab-click="handleClick">
            <el-tab-pane label="Thông tin chung" name="first">  
              <el-row > 
                <el-col :span="8" style="padding:5px">
                  <label>MMSI</label>
                  <el-form-item prop="mmsi" label-width="0"><br/>
                    <el-input :maxlength="10" placeholder="MMSI" v-model="addVessel.mmsi" show-word-limit></el-input>
                  </el-form-item> 
                  <label>Đội tàu</label>
                  <el-form-item label="" prop="crew" label-width="0" ><br/>
                    <el-input placeholder="Đội tàu" v-model="addVessel.crew"></el-input>
                  </el-form-item> 
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>Tên tàu</label>
                  <el-form-item prop="vesselName" label-width="0"><br/>
                    <el-input placeholder="Tên tàu" v-model="addVessel.vesselName" :maxlength="100" show-word-limit></el-input>
                  </el-form-item> 
                  <label>Chiều dài (mét)</label>
                  <el-form-item prop="width" label-width="0"><br/>
                    <el-input-number v-model="addVessel.width" style="with:100%"></el-input-number>
                  </el-form-item> 
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>IMO</label>
                  <el-form-item prop="imo" label-width="0" ><br/>
                     <el-input-number v-model="addVessel.imo" style="with:100%"></el-input-number>                  
                  </el-form-item> 
                  <label>Chiều rộng (mét)</label>
                  <el-form-item prop="height" label-width="0" ><br/>
                    <el-input-number v-model="addVessel.height" style="with:100%"></el-input-number>                  
                  </el-form-item> 
                </el-col>
              </el-row>  
            </el-tab-pane>

            <el-tab-pane label="Thông tin kỹ thuật" name="second">
              <el-row > 
                <el-col :span="8" style="padding:5px">
                  <label>Quốc tịch</label>
                  <el-form-item prop="country" label-width="0"><br/>
                    <el-select  v-model="countryInfo.id" filterable placeholder="Chọn quốc tịch"  @change="changeCountry()" style="width :100%">
                        <el-option v-for="item in list_country" :key="item.id" :label="item.name" :value="item.id" >                          
                        </el-option>
                    </el-select>
                  </el-form-item> 
                  <label>Số điện thoại vệ tinh</label>
                  <el-form-item  prop="satellitePhoneCode" label-width="0" ><br/>
                    <el-input placeholder="Số điện thoại vệ tinh" v-model="addVessel.satellitePhoneCode"></el-input>
                  </el-form-item> 
                  <label>Tốc độ trung bình (knot)</label>
                  <el-form-item label="" prop="speedAvg" label-width="0" ><br/>
                    <el-input-number v-model="addVessel.speedAvg"></el-input-number>
                  </el-form-item> 
                  <label>Tải trọng khi không tải (tấn)</label>
                  <el-form-item label="" prop="deadWeight" label-width="0" ><br/>
                    <el-input-number placeholder="Tải trọng khi không tải (tấn)" v-model="addVessel.deadWeight"></el-input-number>
                  </el-form-item>                   
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>Loại tàu</label>
                  <el-form-item prop="vesselTypeName" label-width="0"><br/>
                     <el-select v-model="vesselTypeInfo.id" filterable placeholder="Loại tàu" @change="changeVesselType()" style="width :100%">
                        <el-option v-for="item in list_vesselType" :key="item.id" :label="item.typeName" :value="item.id"></el-option>
                     </el-select>
                  </el-form-item> 
                  <label>Mớn nước khi không tải</label>
                  <el-form-item prop="draugth" label-width="0"><br/>
                    <el-input-number v-model="addVessel.draugth" style="with:100%"></el-input-number>
                  </el-form-item> 
                  <label>Tốc độ tối đa (knot)</label>
                  <el-form-item label="" prop="speedMax" label-width="0" ><br/>
                    <el-input-number v-model="addVessel.speedMax"></el-input-number>
                  </el-form-item>                   
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>Hô hiệu</label>
                  <el-form-item prop="callsign" label-width="0" ><br/>
                     <el-input v-model="addVessel.callsign" style="with:100%"></el-input>                  
                  </el-form-item> 
                  <label>Thuyên chuyển</label>
                  <el-form-item prop="displacement" label-width="0" ><br/>
                    <el-input v-model="addVessel.displacement" style="with:100%"></el-input>                  
                  </el-form-item> 
                  <label>Tổng tải trọng (tấn)</label>
                  <el-form-item label="" prop="grossTonnage" label-width="0" ><br/>
                    <el-input-number v-model="addVessel.grossTonnage"></el-input-number>
                  </el-form-item>  
                </el-col>
              </el-row>                  
            </el-tab-pane>

            <el-tab-pane label="Sở hữu & quản lý" name="third">
              <el-row > 
                <el-col :span="8" style="padding:5px">
                  <label>Năm sản xuất</label>
                  <el-form-item prop="yearOfBuild" label-width="0"><br/>
                    <el-input-number placeholder="Năm sản xuất" v-model="addVessel.yearOfBuild"></el-input-number>
                  </el-form-item> 
                  <label>Loại động cơ</label>
                  <el-form-item label="" prop="engineType" label-width="0" ><br/>
                    <el-input placeholder="Loại động cơ" v-model="addVessel.engineType"></el-input>
                  </el-form-item> 
                  <label>Tình trạng</label>
                  <el-form-item label="" prop="status" label-width="0" ><br/>
                    <el-input placeholder="Tình trạng" v-model="addVessel.status"></el-input>
                  </el-form-item>               
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>Nơi sản xuất</label>
                  <el-form-item prop="namePlace" label-width="0"><br/>
                     <el-select v-model="placeOfBuildInfo.id" filterable placeholder="Quốc gia" @change="changeplaceOfBuild()" style="width :100%">
                        <el-option v-for="item in list_placeOfBuild" :key="item.id" :label="item.name" :value="item.id"></el-option>
                     </el-select>
                  </el-form-item> 
                  <label>Chủ sở hữu</label>
                  <el-form-item prop="owner" label-width="0"><br/>
                    <el-input v-model="addVessel.owner" style="with:100%"></el-input>
                  </el-form-item> 
                  <label>Thông tin khác</label>
                  <el-form-item label="" prop="otherInfo" label-width="0" ><br/>
                    <el-input placeholder="Thông tin khác" v-model="addVessel.otherInfo"></el-input>
                  </el-form-item>                   
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>Đơn vị</label>
                  <el-form-item prop="unit" label-width="0" ><br/>
                     <el-input placeholder="Đơn vị" v-model="addVessel.unit" style="with:100%"></el-input>                  
                  </el-form-item> 
                  <label>Đơn vị vận hành</label>
                  <el-form-item prop="operationUnit" label-width="0" ><br/>
                    <el-input placeholder="Đơn vị vận hành" v-model="addVessel.operationUnit" style="with:100%"></el-input>                  
                  </el-form-item> 
                </el-col>
              </el-row>  
            </el-tab-pane>

            <el-tab-pane label="Ảnh đại diện" name="fourth">
                <el-form-item label="Ảnh đại diện" prop="imagePath">
                    <img class="image" v-if="imagePath" :src="imagePath" style="max-width: 100%"/>
                    <el-button type="primary" style="display: block" @click="imagecropperShow=true">
                      Upload avatar
                    </el-button>
                    <image-cropper
                      v-show="imagecropperShow"
                      :key="imagecropperKey"
                      :width="200"
                      :height="200"
                      field="file"
                      url="upload/contact/vessel"
                      lang-type="vi"
                      @close="closeChangeAvatar"
                      @crop-upload-success="cropSuccess"
                    />
                </el-form-item>              
            </el-tab-pane>

          </el-tabs>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addVesselVisiable = false">Bỏ qua</el-button>
          <el-button type="primary" @click="addVesselInfo()">Đồng ý</el-button>
        </div>  
    </el-dialog>

      <!-- form edit Vessel -->
      <el-dialog top="5vh"  title="Thông tin" :visible.sync="changeVesselVisiable" width="70%" :close-on-click-modal="false" >
        <el-form ref="editVesselForm" :model="editVessel" :rules="rules" label-position="left" label-width="180px" class="frmEditVessel">
          <el-tabs v-model="activeNameFormEdit" @tab-click="handleClick">
            <el-tab-pane label="Thông tin chung" name="first">  
              <el-row > 
                <el-col :span="8" style="padding:5px">
                  <label>MMSI</label>
                  <el-form-item prop="mmsi" label-width="0"><br/>
                    <el-input :disabled="true" placeholder="MMSI" v-model="editVessel.mmsi"></el-input>
                  </el-form-item> 
                  <label>Đội tàu</label>
                  <el-form-item label="" prop="crew" label-width="0" ><br/>
                    <el-input placeholder="Đội tàu" v-model="editVessel.crew"></el-input>
                  </el-form-item> 
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>Tên tàu</label>
                  <el-form-item prop="vesselName" label-width="0"><br/>
                    <el-input placeholder="Tên tàu" v-model="editVessel.vesselName" :maxlength="100"></el-input>
                  </el-form-item> 
                  <label>Chiều dài (mét)</label>
                  <el-form-item prop="width" label-width="0"><br/>
                    <el-input-number v-model="editVessel.width" style="with:100%"></el-input-number>
                  </el-form-item> 
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>IMO</label>
                  <el-form-item prop="imo" label-width="0" ><br/>
                     <el-input-number v-model="editVessel.imo" style="with:100%"></el-input-number>                  
                  </el-form-item> 
                  <label>Chiều rộng (mét)</label>
                  <el-form-item prop="height" label-width="0" ><br/>
                    <el-input-number v-model="editVessel.height" style="with:100%"></el-input-number>                  
                  </el-form-item> 
                </el-col>
              </el-row>  
            </el-tab-pane>

            <el-tab-pane label="Thông tin kỹ thuật" name="second">
              <el-row > 
                <el-col :span="8" style="padding:5px">
                  <label>Quốc tịch</label>
                  <el-form-item prop="country" label-width="0"><br/>
                    <el-select  v-model="countryInfo.id" filterable placeholder="Chọn quốc tịch"  @change="changeCountry()" style="width :100%">
                        <el-option v-for="item in list_country" :key="item.id" :label="item.name" :value="item.id" >                          
                        </el-option>
                    </el-select>
                  </el-form-item> 
                  <label>Số điện thoại vệ tinh</label>
                  <el-form-item  prop="satellitePhoneCode" label-width="0" ><br/>
                    <el-input placeholder="Số điện thoại vệ tinh" v-model="editVessel.satellitePhoneCode"></el-input>
                  </el-form-item> 
                  <label>Tốc độ trung bình (knot)</label>
                  <el-form-item label="" prop="speedAvg" label-width="0" ><br/>
                    <el-input-number v-model="editVessel.speedAvg"></el-input-number>
                  </el-form-item> 
                  <label>Tải trọng khi không tải (tấn)</label>
                  <el-form-item label="" prop="deadWeight" label-width="0" ><br/>
                    <el-input-number placeholder="Tải trọng khi không tải (tấn)" v-model="editVessel.deadWeight"></el-input-number>
                  </el-form-item>                   
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>Loại tàu</label>
                  <el-form-item prop="vesselTypeName" label-width="0"><br/>
                     <el-select v-model="vesselTypeInfo.id" filterable placeholder="Loại tàu" @change="changeVesselType()" style="width :100%">
                        <el-option v-for="item in list_vesselType" :key="item.id" :label="item.typeName" :value="item.id"></el-option>
                     </el-select>
                  </el-form-item> 
                  <label>Mớn nước khi không tải</label>
                  <el-form-item prop="draugth" label-width="0"><br/>
                    <el-input-number v-model="editVessel.draugth" style="with:100%"></el-input-number>
                  </el-form-item> 
                  <label>Tốc độ tối đa (knot)</label>
                  <el-form-item label="" prop="speedMax" label-width="0" ><br/>
                    <el-input-number v-model="editVessel.speedMax"></el-input-number>
                  </el-form-item>                   
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>Hô hiệu</label>
                  <el-form-item prop="callsign" label-width="0" ><br/>
                     <el-input v-model="editVessel.callsign" style="with:100%"></el-input>                  
                  </el-form-item> 
                  <label>Thuyên chuyển</label>
                  <el-form-item prop="displacement" label-width="0" ><br/>
                    <el-input v-model="editVessel.displacement" style="with:100%"></el-input>                  
                  </el-form-item> 
                  <label>Tổng tải trọng (tấn)</label>
                  <el-form-item label="" prop="grossTonnage" label-width="0" ><br/>
                    <el-input-number v-model="editVessel.grossTonnage"></el-input-number>
                  </el-form-item>  
                </el-col>
              </el-row>                  
            </el-tab-pane>

            <el-tab-pane label="Sở hữu & quản lý" name="third">
              <el-row > 
                <el-col :span="8" style="padding:5px">
                  <label>Năm sản xuất</label>
                  <el-form-item prop="yearOfBuild" label-width="0"><br/>
                    <el-input-number placeholder="Năm sản xuất" v-model="editVessel.yearOfBuild"></el-input-number>
                  </el-form-item> 
                  <label>Loại động cơ</label>
                  <el-form-item label="" prop="engineType" label-width="0" ><br/>
                    <el-input placeholder="Loại động cơ" v-model="editVessel.engineType"></el-input>
                  </el-form-item> 
                  <label>Tình trạng</label>
                  <el-form-item label="" prop="status" label-width="0" ><br/>
                    <el-input placeholder="Tình trạng" v-model="editVessel.status"></el-input>
                  </el-form-item>               
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>Nơi sản xuất</label>
                  <el-form-item prop="namePlace" label-width="0"><br/>
                     <el-select v-model="placeOfBuildInfo.id" filterable placeholder="Quốc gia" @change="changeplaceOfBuild()" style="width :100%">
                        <el-option v-for="item in list_placeOfBuild" :key="item.id" :label="item.name" :value="item.id"></el-option>
                     </el-select>
                  </el-form-item> 
                  <label>Chủ sở hữu</label>
                  <el-form-item prop="owner" label-width="0"><br/>
                    <el-input v-model="editVessel.owner" style="with:100%"></el-input>
                  </el-form-item> 
                  <label>Thông tin khác</label>
                  <el-form-item label="" prop="otherInfo" label-width="0" ><br/>
                    <el-input placeholder="Thông tin khác" v-model="editVessel.otherInfo"></el-input>
                  </el-form-item>                   
                </el-col>

                <el-col :span="8" style="padding:5px">
                  <label>Đơn vị</label>
                  <el-form-item prop="unit" label-width="0" ><br/>
                     <el-input placeholder="Đơn vị" v-model="editVessel.unit" style="with:100%"></el-input>                  
                  </el-form-item> 
                  <label>Đơn vị vận hành</label>
                  <el-form-item prop="operationUnit" label-width="0" ><br/>
                    <el-input placeholder="Đơn vị vận hành" v-model="editVessel.operationUnit" style="with:100%"></el-input>                  
                  </el-form-item> 
                </el-col>
              </el-row>  
            </el-tab-pane>

            <el-tab-pane label="Ảnh đại diện" name="fourth">
                <el-form-item label="Ảnh đại diện" prop="imagePath">
                    <img class="image" v-if="imagePath" :src="imagePath" style="max-width: 100%"/>
                    <el-button type="primary" style="display: block" @click="imagecropperShow=true">
                      Upload avatar
                    </el-button>
                    <image-cropper
                      v-show="imagecropperShow"
                      :key="imagecropperKey"
                      :width="200"
                      :height="200"
                      field="file"
                      url="upload/contact/vessel"
                      lang-type="vi"
                      @close="closeChangeAvatar"
                      @crop-upload-success="cropSuccess"
                    />
                </el-form-item>              
            </el-tab-pane>

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
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="changeVesselVisiable = false">Bỏ qua</el-button>
          <el-button type="primary" @click="updateVesselInfo()">Đồng ý</el-button>
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
import ImageCropper from '@/components/ImageCropper';
import { validString } from "@/utils";

const vesselDetailResource = new Resource("contact/vessel/detail/getDetailVessel")
const vesselResource = new Resource("ais/vessel/search-list");
const vesselCrudResource = new Resource("contact/vessel");
const countriesResource = new Resource("ais/country");
const vesselTypeResource = new Resource("ais/vessel-type");

//api add,del ip và sdt dùng chung của headquater
const addIpResource = new Resource("contact/land/addIpHeadQuarter"); 
const deleteIPLandResource = new Resource("contact/land/delIpHeadQuarter");
const addPhoneResource = new Resource("contact/land/addPhoneHeadQuarter"); 
const deletePhoneLandResource = new Resource("contact/land/delPhoneHeadQuarter");


var stoteFile = process.env.VUE_APP_FILE_API + "upload";
export default {
  components: {
    Pagination,
    ImageCropper,
  },
  directives: { waves },
  data() {
    return {    
      changeVesselVisiable:false,
      addVesselVisiable:false,
      activeNameFormEdit:'first',
      value: [1],
      multiple: true,
      search: true,
      controls: true,
      isOpen: false,
      activeNames: '0',
      list: null,
      total: 0,
      totalSDT:0,
      totalIP:0,
      loading: true,
      imagePath : "",
      query: {
          currentPage: 1,
          rowsPerPage: 10,
          sort: "createdAt",
          mmsi: "",
          vesselName: "",
          countryId : null,
          vesselTypeId: null,
          ip :"",
          keyword: "",
          advanceSearch : 0,
          advancePhone : "",  
          advancecallSign : "",
          advanceMmsiMax: "",
          advanceMmsiMin: "",
          advanceHeightMin :"",
          advanceHeightMax :"",
          advanceIMOMin :"",
          advanceIMOMax:"",
          advanceWidthMin :"",
          advanceWidthMax :"",


      },
      typeSelect:null,
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

      list_country:null,
      list_countrySearch :[{
        id:null,
        name:null
      }],
      list_vesselType:null,
      list_vesselTypeSearch :[{
        id:null,
        name:null
      }],

      list_placeOfBuild:null,
      placeOfBuildInfo:{
        name : "", 
        id : 0,
      },

      valueCountrySearch:'',
      countryInfo:{
        name : "", 
        id : 0,
      },
      valueVesselTypeSearch:'',
      vesselTypeInfo:{
        name : "", 
        id : 0,
      },

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

      editVessel: {
        mmsi:0,
        vesselName : '',           //tên tàu
        vesselTypeName :'',        //tên loại tàu
        vesselType:0,              // loại tàu
        imo: 0,
        imagePath:"",
        crew: '',                  //đội tàu
        satellitePhoneCode :'',    //số điện thoại vệ tinh
        countryId: 0,
        countryName :'',
        width : 0,
        height : 0,
        callsign: '',
        createdTime: null,
        updatedTime: null,
        nameStatic: '',     
        draugth : 0,               // Mớn nước khi không tải  
        displacement : '',         //Thuyên chuyển
        speedAvg : 0 ,             //Tốc độ trung bình (knot)
        speedMax : 0 ,             //Tốc độ tối đa (knot)
        grossTonnage : 0 ,         // Tổng Tải trọng 
        yearOfBuild : 0 ,       //năm sản xuất
        placeOfBuildCode : 0 ,  // nơi sản xuất id
        namePlace:'',           // nơi sản xuất name
        unit : '',                 //đơn vị  (thiếu api)  -> vessel_alt_info 
        owner : '',                //chủ sở hữu (thiếu api) -> vessel_alt_info 
        operationUnit :'',         //Đơn vị vận hành (thiếu api) -> vessel_alt_info 
        otherInfo : '',            //Thông tin khác
        engineType : '',           //Loại động cơ
        status : '',               //tình trạng 
        ips:null,
        phones: null,
        deadWeight: 0,             // Tải trọng khi không tải (tấn)    
        weapons : '',  
        endurance : '', 
        userUpdate :'',             //user update
      },
      addVessel: {
        mmsi:null,
        vesselName : '',           //tên tàu
        vesselTypeName :'',
        imo: 0,
        imagePath:"",
        crew: '',                  //đội tàu
        satellitePhoneCode :'',    //số điện thoại vệ tinh
        countryId: 0,
        countryName :'',
        width : 0,
        height : 0,
        callsign: '',
        createdTime: null,
        updatedTime: null,
        nameStatic: '',     
        draugth : 0,               // Mớn nước khi không tải  
        displacement : '',         //Thuyên chuyển
        speedAvg : 0 ,             //Tốc độ trung bình (knot)
        speedMax : 0 ,             //Tốc độ tối đa (knot)
        grossTonnage : 0 ,         // Tổng Tải trọng 
        yearOfBuild : 0 ,       //năm sản xuất
        placeOfBuildCode : 0 ,  // nơi sản xuất
        namePlace: '',           // nơi sản xuất name
        unit : '',                 //đơn vị  (thiếu api)  -> vessel_alt_info 
        owner : '',                //chủ sở hữu (thiếu api) -> vessel_alt_info 
        operationUnit :'',         //Đơn vị vận hành (thiếu api) -> vessel_alt_info 
        otherInfo : '',            //Thông tin khác
        engineType : '',           //Loại động cơ
        status : '',               //tình trạng 
        deadWeight: 0,             // Tải trọng khi không tải (tấn)       
        weapons : '',
        endurance :'',
        userUpdate :'',           //user update
      },

      images:[],
      ips:null,
      phones : null,
      num: 1,
      rules: {
        mmsi: [{ required: true, message: 'MMSI bắt buộc nhập', trigger: 'blur' }],
        vesselName : [{ required: true, message: 'Tên tàu bắt buộc nhập', trigger: 'blur' }]
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
    this.getList();
    this.getRoleMenuPage();
    this.getListVesselType();
    this.getListCountries();
  },
  methods: {
    async getRoleMenuPage(){
      console.log(this.$store.getters.roleMenuActions);
      for(var i=0; i<this.$store.getters.roleMenuActions.length;i++){
        if(this.$store.getters.roleMenuActions[i].pageUrl == "/contact/vessel"){
          this.checkCreateRole = this.$store.getters.roleMenuActions[i].canCreate;
          this.checkEditRole = this.$store.getters.roleMenuActions[i].canUpdate;
          this.checkViewRole = this.$store.getters.roleMenuActions[i].canRead;
          this.checkDeleteRole = this.$store.getters.roleMenuActions[i].canDelete;
        }
      }
    },    

    validateIP(ipAddress){    
      if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipAddress))
      {
        return true;
      }else{
        return false;
      }

    },
    
    handleChange(val) {
        console.log(val);
       
    },
    handleFilterAdvance(data) {
      console.log("handleFilterAdvance : " + data);

    },

    handleFilter(){
      console.log("handleFilter : " + this.isOpen);
      if(this.isOpen){
        this.query.advanceSearch = 1
        var errMessAdvance = "";
        if(this.query.advanceMmsiMax != "" && this.query.advanceMmsiMin == ""){
          errMessAdvance = "Bạn phải nhập MMSI nhỏ nhất"
        }else if(this.query.advanceMmsiMax == "" && this.query.advanceMmsiMin != ""){
          errMessAdvance = "Bạn phải nhập MMSI lớn nhất"
        }
        else if(isNaN(this.query.advanceHeightMin) && (this.query.advanceHeightMin != "")){
          errMessAdvance = "Chiều rộng nhỏ nhất phải là kiểu số"
        }else if(isNaN(this.query.advanceHeightMax) && (this.query.advanceHeightMax != "")){
          errMessAdvance = "Chiều rộng lớn nhất phải là kiểu số"
        }else if(this.query.advanceHeightMax == "" && this.query.advanceHeightMin !=""){
           errMessAdvance = "Bạn phải nhập chiều rộng lớn nhất"
        }else if(this.query.advanceHeightMax != "" && this.query.advanceHeightMin ==""){
           errMessAdvance = "Bạn phải nhập chiều rộng nhỏ nhất"
        }

        else if(isNaN(this.query.advanceIMOMin) && (this.query.advanceIMOMin != "")){
          errMessAdvance = "IMO nhỏ nhất phải là kiểu số"
        }else if(isNaN(this.query.advanceIMOMax) && (this.query.advanceIMOMax != "")){
          errMessAdvance = "IMO lớn nhất phải là kiểu số"
        }else if(this.query.advanceIMOMin == "" && this.query.advanceIMOMin !=""){
           errMessAdvance = "Bạn phải nhập IMO lớn nhất"
        }else if(this.query.advanceIMOMax != "" && this.query.advanceIMOMax ==""){
           errMessAdvance = "Bạn phải nhập IMO nhỏ nhất"
        }  
        
        else if(isNaN(this.query.advanceWidthMin) && (this.query.advanceWidthMin != "")){
          errMessAdvance = "Chiều dài nhỏ nhất phải là kiểu số"
        }else if(isNaN(this.query.advanceWidthMax) && (this.query.advanceWidthMax != "")){
          errMessAdvance = "Chiều dài lớn nhất phải là kiểu số"
        }else if(this.query.advanceWidthMin == "" && this.query.advanceWidthMin !=""){
           errMessAdvance = "Bạn phải nhập Chiều dài lớn nhất"
        }else if(this.query.advanceWidthMax != "" && this.query.advanceWidthMax ==""){
           errMessAdvance = "Bạn phải nhập chiều dài nhỏ nhất"
        }                

        if(errMessAdvance != ""){
            this.$notify({
              title: 'Có lỗi xảy ra',
              message: errMessAdvance,
              type: 'error'
            });    
            return;
        }

      }else{
        this.query.advanceSearch = 0;
      }
      if (validString(this.query.keyword)) {
            this.$notify({
              title: 'Có lỗi xảy ra',
              message: 'Từ khóa không được chưa ký tự đặc biệt.',
              type: 'error'
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
    async getListVesselType(){
      vesselTypeResource.list().then((response) => {
            if (response.status === 200) {
              this.list_vesselType = response.data;
              this.list_vesselTypeSearch = response.data;
              this.vesselTypeInfo = {
                id : response.data[0].id,
                name : response.data[0].typeName              
              };
              
            }
      })
      .catch((err) => {
            console.log(err);
      }); 
    },
    async getListCountries(){
      countriesResource.list().then((response) => {
          if (response.status === 200) {
              this.list_country = response.data;
              this.list_countrySearch = response.data;
              this.list_placeOfBuild = response.data;

              this.countryInfo = {
                id : response.data[0].id,
                name : response.data[0].name              
              };

              this.placeOfBuildInfo = {
                id : response.data[0].id,
                name : response.data[0].name              
              };
            }
      })
      .catch((err) => {
            console.log(err);
      }); 
    },

    async getList() {
      this.loading = true;
      this.query.keyword = this.query.keyword.trim();
      const { data, total } = await vesselResource.listByPost(this.query);
      this.list = data;

      if (data) {
        this.total = total;
      }else{
        this.total = 0;
      }
      this.loading = false;
    },

    handleChangeTypeSelect(){
      console.log("handleChangeTypeSelect");
    },
    handleAddVessel(){
      console.log("handleAddVessel");
      this.addVessel.mmsi = null;
      this.addVessel.vesselName = '';
      this.addVessel.imo = 0;
      this.addVesselVisiable = true;
      this.imagePath = "";
    },

    handleChangeVessel(data) {
       console.log("mrd : " +data.mmsi);
        if(data) { // Fill data into model for update row
           vesselDetailResource.listByPost({mmsi:data.mmsi}).then((response) => {
              if (response.status === 200) {
                this.editVessel = {
                  mmsi : response.data.info.mmsi, 
                  vesselName : response.data.info.name, 
                  imo : response.data.info.imo, 
                  crew : response.data.info.crew, 
                  width : response.data.info.chieudai,
                  height : response.data.info.chieurong,  
                  countryId : response.data.info.country_id,  
                  satellitePhoneCode : response.data.info.satellitePhoneCode,
                  vesselTypeName : response.data.info.vesselTypeName,
                  callsign : response.data.info.callsign,
                  draugth : response.data.info.draugth,
                  displacement : response.data.info.displacement , 
                  speedAvg : response.data.info.speedAvg,
                  speedMax : response.data.info.speedMax,
                  grossTonnage : response.data.info.grossTonnage,   
                  createdTime : response.data.info.createdTime,  
                  updatedTime : response.data.info.updatedTime,  
                  deadWeight :  response.data.info.deadWeight,   
                  yearOfBuild:  response.data.info.yearOfBuild,
                  placeOfBuildCode : response.data.info.placeOfBuildCode,  
                  engineType : response.data.info.engineType,
                  owner : response.data.vesselAltInfo.owner,
                  operationUnit : response.data.vesselAltInfo.operationUnit,
                  otherInfo : response.data.vesselAltInfo.otherInfo,
                  status : response.data.vesselAltInfo.status,
                  unit: response.data.vesselAltInfo.unit,

                         
                };   
                this.countryInfo = {
                  id : response.data.info.country_id,
                  name : response.data.info.countryName,    
                };
                 this.vesselTypeInfo = {
                  id : response.data.info.type_id,
                  name : response.data.info.vesselTypeName,    
                };
                this.placeOfBuildInfo = {
                  id : response.data.info.placeOfBuildCode,
                  name : response.data.info.namePlace,    
                };
                
                this.ips = response.data.ips;
                this.phones = response.data.phones;
                this.addIpAddress.id =response.data.info.mmsi;
                this.addPhone.id = response.data.info.mmsi;
                this.addIpAddress.note = '';
                this.addIpAddress.ip_address = '';
                this.imagePath = "";
                console.log("handleChangeVessel :" + response.data);               
                this.changeVesselVisiable = true;
              }
            })
            .catch((err) => {
              console.log(err);
            });        
        }

    },
    handleDeleteVessel(data) {
      console.log("handleDeleteVessel : " + data);
        this.$confirm('Bạn có chắc chắn xóa', 'Cảnh báo', {
          confirmButtonText: 'Đồng ý',
          cancelButtonText: 'Hủy bỏ',
          type: 'warning',
        })
        .then(() => {
          vesselCrudResource.destroy(data)
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
    
    cropSuccess(resData) {
      console.log("cropSuccess")
      this.imagePath = resData.data[0].fileDownloadUri;
    },

    closeChangeAvatar() {
          this.imagecropperShow = false;
    },

    getIPByMMSI(){
      console.log("getIPByMMSI");
    },
    getSDTByMMSI(){
      console.log("getSDTByMMSI");
    },

    reloadDetailVesel(val){
           vesselDetailResource.listByPost({mmsi:val}).then((response) => {             
              if (response.status === 200) {
                 this.ips = response.data.ips;
                 this.phones = response.data.phones;

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
                if (response.status === 200 && response.data != null) {
                    this.$notify({
                          title: "Thành Công",
                          message: "Thêm IP thành công!",
                          type: "success",
                    });
                    this.addIpAddress.ip_address = '';
                    this.addIpAddress.note = '';
                    this.getList();
                    this.reloadDetailVesel(this.addIpAddress.id);
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
                    this.reloadDetailVesel(this.addPhone.id);
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
    handleDeleteIP(ip){
      console.log("handleDeleteIP : " + ip);
      this.deleteIp.id = this.editVessel.mmsi;
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
            this.reloadDetailVesel(this.deleteIp.id);
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
      this.deletePhone.id = this.editVessel.mmsi;
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
            this.reloadDetailVesel(this.deletePhone.id);
          })
          .catch(err => {
            console.log(err);
          })
        })
        .catch(() => {
          console.log('cancel');
        });
    },


    async addVesselInfo(){
      console.log("mrd addVesselInfo");
      this.$refs.addVesselForm.validate((valid) => {
          if (valid) {         
            if(isNaN(this.addVessel.mmsi)){              
                this.$notify({
                      title: 'Lỗi',
                      message: "MMSI phải là số ",
                      type: 'error'
                });
            }else if(this.addVessel.mmsi.includes(".")){
                this.$notify({
                      title: 'Lỗi',
                      message: "MMSI phải là số nguyên",
                      type: 'error'
                });
            } else {
              if(this.imagePath != ""){
                this.addVessel.imagePath = this.imagePath.substring(stoteFile.length,this.imagePath.length);
              }   
              vesselCrudResource.store(this.addVessel).then((response) => {
              if (response.status === 200 && response.data != null) {
                    this.$notify({
                      title: 'Thành công',
                      message: "Thêm thành công",
                      type: 'success'
                    });
                  this.getList();
                  this.addVesselVisiable = false;
              }else
                    this.$notify({
                      title: 'Lỗi',
                      message: response.message,
                      type: 'error'
                    });
              })
              .catch((err) => {
                    this.$notify({
                      title: 'Lỗi',
                      message: err,
                      type: 'error'
                    });
              }); 
            }       

          
          }
      });      

    },

    async updateVesselInfo(){
      console.log("mrd updateVesselInfo");
      this.$refs.editVesselForm.validate((valid) => {
          if (valid) {     
          this.editVessel.countryId = this.countryInfo.id;   
          this.editVessel.vesselType = this.vesselTypeInfo.id;  
          this.editVessel.placeOfBuildCode = this.placeOfBuildInfo.id;      
          if(this.imagePath != ""){
            this.editVessel.imagePath = this.imagePath.substring(stoteFile.length,this.imagePath.length);
          }      
          vesselCrudResource.updateWithResource(this.editVessel).then((response) => {
              if (response.status === 200) {
                  this.changeVesselVisiable = false;
                  this.query.currentPage = 1;
                  this.$notify({
                  title: 'Thành công',
                  message: 'Sửa thành công',
                  type: 'success'
                });
                 this.getList();
              }else
                  alert('Thêm/sửa lỗi!');
              })
              .catch((err) => {
              console.log(err);
              }); 
          
          }
          
      });         
    },

    changeCountrySearch(){
      console.log("changeCountrySearch");
    },

    changeCountry(){
      console.log(" this.editVessel.countryId : " + this.countryInfo.id);   
      console.log("this.editVessel.countryName: " + this.countryInfo.name);  
      console.log("changeCountry");
    },
    changeVesselType(){
      console.log("changeVesselType");
    },

    changeVesselTypeSearch(){
      console.log("changeVesselTypeSearch");
    },
    changeplaceOfBuild(){
      console.log("---------changeplaceOfBuild-------------");
      console.log(" this.editVessel.countryInfo : " + JSON.stringify(this.countryInfo)); 
      console.log(" this.editVessel.placeOfBuildCode : " + JSON.stringify(this.placeOfBuildInfo));   

    }


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
.el-textarea__inner {
    height: 200px;
}

.el-form-item--medium .el-form-item__content, .el-form-item--medium .el-form-item__label{
  line-height: 15px;
}

.claim td .el-button {
  margin-left: 0;
}


</style>
