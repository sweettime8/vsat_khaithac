<template>
  <div style="width: 100%; height: 100vh">
    <!-- Popup -->
    <div id="popup" class="ol-popup dialog-detail-vessel">
      <a
        href="#"
        id="popup-closer"
        class="ol-popup-closer"
        @click="closerPopup()"
      ></a>
      <div id="popup-content" v-show="isShowVesselDetailDialog">
        <h3 style="margin-top: 6px">Mã tàu: {{ vesselDetailDialog.objId }}</h3>
        <!-- <label>Thông tin chung</label> -->
        <br />
        <span
          ><b>Hình ảnh:</b>
          <img
            v-bind:src="vesselDetailDialog.imagePath"
            alt="no image"
            width="50"
            height="50"
          />
        </span>
        <br />
        <b-row>
          <b-col sm="12">
            <span
              ><b>Tên tàu: </b>
              <span v-tooltip="{ content: vesselDetailDialog.vesselName }">{{
                vesselDetailDialog.vesselName
              }}</span>
            </span>
          </b-col>
          <b-col sm="12" class="text-des">
            <span
              ><b>Loại tàu: </b>
              <span
                id="vesselType"
                v-tooltip="{ content: vesselDetailDialog.vesselTypeName }"
                >{{ vesselDetailDialog.vesselTypeName }}</span
              >
            </span>
          </b-col>
        </b-row>
        <b-row>
          <b-col sm="12">
            <span
              ><b>Quốc gia: </b>
              <span>{{ vesselDetailDialog.countryName }}</span>
            </span>
          </b-col>
        </b-row>
        <b-row>
          <b-col sm="6">
            <span
              ><b>Dài: </b>
              <span>{{ vesselDetailDialog.length }}</span>
            </span>
          </b-col>
          <b-col sm="6" class="text-des">
            <span
              ><b>rộng: </b>
              <span id="vesselType">{{ vesselDetailDialog.width }}</span>
            </span>
          </b-col>
        </b-row>
        <b-row>
          <b-col sm="6">
            <span
              ><b>Mớm nước: </b>
              <span>{{ vesselDetailDialog.draugth }}(m)</span>
            </span>
          </b-col>
          <b-col sm="6" class="text-des">
            <span
              ><b>Hô hiệu: </b>
              <span id="vesselType">{{ "Chưa gán" }}</span>
            </span>
          </b-col>
        </b-row>
        <b-row>
          <b-col sm="6">
            <span
              ><b>Tốc độ: </b>
              <span>{{ vesselDetailDialog.sog }}(knot)</span>
            </span>
          </b-col>
          <b-col sm="6" class="text-des">
            <span
              ><b>Hướng: </b>
              <span id="vesselType">{{ vesselDetailDialog.cog }}(°)</span>
            </span>
          </b-col>
        </b-row>
        <b-row>
          <b-col sm="6">
            <span
              ><b>Vĩ độ: </b>
              <span>{{ vesselDetailDialog.latitude }}</span>
            </span>
          </b-col>
          <b-col sm="6" class="text-des">
            <span
              ><b>Kinh độ: </b>
              <span id="vesselType">{{ vesselDetailDialog.longitude }}</span>
            </span>
          </b-col>
        </b-row>
        <b-row>
          <b-col sm="6">
            <span
              ><b>Ip Nguồn: </b>
              <span>{{ vesselDetailDialog.sourceIp }}</span>
            </span>
          </b-col>
          <b-col sm="6" class="text-des">
            <span
              ><b>Ip Đích: </b>
              <span id="vesselType">{{ vesselDetailDialog.destIp }}</span>
            </span>
          </b-col>
        </b-row>
        <b-row>
          <b-col sm="12">
            <span
              ><b>Chiều dữ liệu: </b>
              <span>{{ "chưa gán" }}</span>
            </span>
          </b-col>
        </b-row>
        <b-row>
          <b-col sm="12">
            <span
              ><b>Nơi đến: </b>
              <span>{{ vesselDetailDialog.destination }}</span>
            </span>
          </b-col>
        </b-row>
        <b-row>
          <b-col sm="12">
            <span
              ><b>Ngày đến: </b>
              <span>{{ "Chưa gán" }}</span>
            </span>
          </b-col>
        </b-row>
        <b-row>
          <b-col sm="12">
            <span
              ><b>Cập nhật: </b>
              <span>{{ vesselDetailDialog.ingestTime }}</span>
            </span>
          </b-col>
        </b-row>
        <br />
        <el-button
          type="primary"
          size="small"
          @click="handleGetVoyageDetail(vesselDetailDialog)"
          >Chi tiết hành trình</el-button
        >
      </div>
      <!-- <div id="popup-content" v-show="!isShowVesselDetailDialog">
        <label><b>Vị trí </b> </label>
        <br />
        <span
          >Kinh độ:
          <span>{{ positionInfo.longitude }}</span>
        </span>
        <br />
        <span
          >Vĩ độ:
          <span id="vesselType">{{ positionInfo.latitude }}</span>
        </span>
        <br />
      </div> -->
    </div>
    <!-- <el-button class="search" @click="handleDraw()" type="primary"
      >Draw</el-button> -->

    <div class="el-button-on-map">
      <el-tooltip
        class="item"
        effect="dark"
        content="Trung tâm bản đồ"
        placement="left-start"
      >
        <el-button
          class="search"
          style="background-position: center; margin: 9px"
          :style="{
            backgroundImage:
              'url(' + require('@/assets/icon/Group 1177.png') + ')',
          }"
          @click="centerToPoisition(null, null, 3)"
          type="primary"
        >
        </el-button>
      </el-tooltip>
      <el-tooltip
        class="item"
        effect="dark"
        content="Clear"
        placement="left-start"
      >
        <el-button
          class="search"
          style="background-position: center;background-color:#0d518c;"
          @click="clearAllFeature()"
          type="primary"
        >
          <i class="el-icon-document-remove"></i>
        </el-button>
      </el-tooltip>
      <el-tooltip
        class="item"
        effect="dark"
        content="Danh sách Vùng"
        placement="left-start"
      >
        <el-button
          class="search"
          @click="openListAreaDialog()"
          style="background-position: center"
          :style="{
            backgroundImage:
              'url(' + require('@/assets/icon/Group 1182.png') + ')',
          }"
          type="primary"
        >
          <!-- <i class="el-icon-place">Danh sách</i> -->
        </el-button>
      </el-tooltip>
      <el-button
        class="search"
        @click="handleDeleteFeature()"
        v-show="selectedFeatureID != null"
        type="primary"
      >
        <i class="el-icon-close"></i>
      </el-button>
      <!-- <el-button
        class="search"
        @click="editArea()"
        v-show="selectedFeatureID != null"
        type="primary"
      >
        <i class="el-icon-edit">Sửa vùng</i>
      </el-button> -->
      <!-- <el-button
        class="search"
        v-show="!vung.isDrawing"
        @click="saveDraw()"
        type="primary"
      >
        <i class="el-icon-upload2">Lưu vùng</i>
      </el-button> -->
      <!-- <el-button
        class="search"
        @click="handleEndDraw()"
        v-show="vung.isDrawing"
        type="primary"
      >
        <i class="el-icon-close"></i>
      </el-button> -->
      <el-tooltip
        class="item"
        effect="dark"
        content="Tạo Vùng"
        placement="left-start"
      >
        <el-button
          class="search"
          style="background-color:#0d518c"
          @click="addArea()"
          v-show="!vung.isDrawing"
          type="primary"
        >
          <i class="el-icon-add-location"></i>
        </el-button>
      </el-tooltip>
      <!-- <el-button
        class="search"
        @click="openDialogLoadListVessel()"
        type="primary"
        ><i class="el-icon-ship"></i
      ></el-button> -->
      <!-- <el-button
        class="search"
        @click="openDialogLoadListVesselGroup()"
        type="primary"
        ><i class="el-icon-truck"></i
      ></el-button> -->

      <!-- hien thị list table tau -->
      <!-- <el-button
        class="search"
        @click="voyage.isShowShipGrid = true"
        type="primary"
        ><i class="el-icon-ship"></i
      ></el-button> -->
    </div>
    <div id="mapOL"></div>
    <div id="mouse-position"></div>
    <el-dialog
      top="5vh"
      v-dialogDrag
      title="Tìm kiếm danh sách vị trí tàu"
      :visible.sync="isShowDialog"
      width="50%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editUserForm"
        :model="editUser"
        label-position="left"
        label-width="180px"
      >
        <b-row class="row">
          <b-col style="padding: 5px"></b-col>
          <b-col style="padding: 5px"></b-col>
          <b-col style="padding: 5px; text-align: right; margin-right: 15px"
            ><el-button
              @click="query.isAdvance = !query.isAdvance"
              type="primary"
              style="bt-search-advance"
              cl
            >
              <span>Tìm kiếm nâng cao</span>
            </el-button></b-col
          >
        </b-row>
        <b-row class="row">
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Từ ngày <span class="require">(*)</span></label>
            </div>
            <el-date-picker
              id="jack"
              v-model="query.startTime"
              size="small"
              format="dd/MM/yyyy HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :editable="false"
              style="width: 200px; margin-left: 10px"
              type="datetime"
              placeholder="Từ ngày"
            ></el-date-picker>
          </b-col>
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Đến ngày <span class="require">(*)</span></label>
            </div>
            <el-date-picker
              v-model="query.endTime"
              size="small"
              format="dd/MM/yyyy HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              :editable="false"
              style="width: 200px; margin-left: 10px"
              type="datetime"
              placeholder="Đến ngày"
            ></el-date-picker>
          </b-col>
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Nhóm tàu</label>
            </div>
            <el-select
              v-model="query.vesselGroup"
              value-key="value"
              placeholder="Nhóm tàu"
              @change="handleChangeVesselGroupSelect"
              style="width: 200px; margin-left: 10px"
            >
              <el-option
                :key="-1"
                :label="'------none------'"
                :value="null"
              ></el-option>
              <el-option
                v-for="item in lstVesselGroup"
                :key="item.id"
                :label="item.name"
                :value="item"
              ></el-option>
            </el-select>
          </b-col>
        </b-row>
        <b-row>
          <b-col :span="8" md="4" style="padding: 5px">
            <div class="label-form">
              <label>Vùng</label>
            </div>
            <el-select
              v-model="query.area"
              value-key="value"
              placeholder="vùng"
              style="width: 200px; margin-left: 10px"
            >
              <el-option
                :key="-1"
                :label="'------none------'"
                :value="null"
              ></el-option>
              <el-option
                v-for="item in lstArea"
                :key="item.id"
                :label="item.name + ' (id: ' + item.id + ')'"
                :value="item"
              ></el-option>
            </el-select>
          </b-col>

          <b-col v-show="query.isAdvance" md="4" style="padding: 5px">
            <div class="label-form">
              <label>Loại tàu</label>
            </div>
            <el-select
              v-model="query.vesselType"
              value-key="value"
              placeholder="Loại tàu"
              style="width: 200px; margin-left: 10px"
            >
              <el-option
                :key="-1"
                :label="'------none------'"
                :value="null"
              ></el-option>
              <el-option
                v-for="item in listVesselType"
                :key="item.id"
                :label="item.typeName + ' (id: ' + item.id + ')'"
                :value="item"
              ></el-option>
            </el-select>
          </b-col>
          <b-col v-show="query.isAdvance" style="padding: 5px" md="4">
            <div class="label-form">
              <label>Quốc gia</label>
            </div>
            <el-select
              v-model="query.country"
              value-key="value"
              placeholder="Quốc gia"
              style="width: 200px; margin-left: 10px"
            >
              <el-option
                :key="-1"
                :label="'------none------'"
                :value="null"
              ></el-option>
              <el-option
                v-for="item in listCountry"
                :key="item.id"
                :label="item.name + ' (id: ' + item.id + ')'"
                :value="item"
              ></el-option>
            </el-select>
          </b-col>
        </b-row>
        <b-row v-show="query.isAdvance">
          <b-col :span="8" style="padding: 5px">
            <div class="label-form">
              <label>Nguồn thu</label>
            </div>
            <!-- multiple -->
            <el-select
              id="sltDataSource"
              collapse-tags
              v-model="query.dataSource"
              clearable
              style="width: 200px; margin-left: 10px"
              placeholder="Nguồn thu"
              size="small"
            >
              <el-option
                v-for="item in lstDataSource"
                :key="item.id"
                :label="item.sourceName"
                :value="item.id"
              ></el-option>
            </el-select>
          </b-col>
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>MMSI</label>
            </div>
            <!-- multiple -->
            <el-input
              v-model="query.mmsi"
              type="text"
              name="mmsi"
              style="width: 200px; margin-left: 10px"
              placeholder="MMSI"
            />
          </b-col>
          <!-- <b-col style="padding: 5px">
            <div class="label-form">
              <label>Số ĐT</label>
            </div>
     
            <el-input
              v-model="query.mobile"
              type="text"
              name="mobile"
              style="width: 200px; margin-left: 10px"
              placeholder="Số điện thoại"
            />
          </b-col> -->
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Ip nguồn</label>
            </div>
            <!-- multiple -->
            <el-input
              v-model="query.sourceIps"
              type="text"
              name="mobile"
              style="width: 200px; margin-left: 10px"
              placeholder="Ip nguồn"
            />
          </b-col>
        </b-row>
        <b-row v-show="query.isAdvance">
          <b-col style="padding: 5px">
            <div class="label-form">
              <label>Ip Đích</label>
            </div>
            <!-- multiple -->
            <el-input
              v-model="query.destIps"
              type="text"
              name="mobile"
              style="width: 200px; margin-left: 10px"
              placeholder="Ip Đích"
            />
          </b-col>
        </b-row>
      </el-form>
      <br />
      <el-table
        id="my-table"
        v-loading="loading"
        :data="listAISShow"
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
            <span>{{ scope.row.objId }}</span>
          </template>
        </el-table-column>

        <el-table-column label="IP nguồn">
          <template slot-scope="scope">
            <span>{{ scope.row.sourceIp }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Ip Đích">
          <template slot-scope="scope">
            <span>{{ scope.row.destIp }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Vĩ độ">
          <template slot-scope="scope">
            <span>{{ scope.row.latitude }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Kinh độ">
          <template slot-scope="scope">
            <span>{{ scope.row.longitude }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Tác động" width="150">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleGetVoyageDetail(scope)"
              >Chi tiết hành trình</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="query.total"
        :page.sync="query.currentPage"
        :perPage="query.perPage"
        :limit.sync="query.rowsPerPage"
        :hidden="query.total < 10"
        @pagination="getListAIS"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShowDialog = false">Bỏ qua</el-button>
        <el-button type="primary" @click="handleClutersMake()"
          >Tìm kiếm danh sách tàu</el-button
        >
        <el-button type="primary" @click="handleClutersMake(true)"
          >Hành trình</el-button
        >
      </div>
    </el-dialog>

    <!-- grid ship -->
    <el-dialog
      v-dialogDrag
      top="5vh"
      title="Danh sách sách vị trí tàu"
      :visible.sync="voyage.isShowShipGrid"
      width="50%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editUserForm"
        :model="editUser"
        label-position="left"
        label-width="180px"
      >
        <el-date-picker
          v-model="query.startTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false"
          style="width: 200px"
          type="datetime"
          placeholder="Từ ngày"
        ></el-date-picker>
        <el-date-picker
          v-model="query.endTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false"
          style="width: 200px; margin-left: 10px"
          type="datetime"
          placeholder="Đến ngày"
        ></el-date-picker>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShowDialog = false">Bỏ qua</el-button>
        <el-button type="primary" @click="handleClutersMake()"
          >Tìm kiếm</el-button
        >
      </div>
    </el-dialog>

    <!-- grid nhóm tàu -->
    <el-dialog
      v-dialogDrag
      top="5vh"
      title="Danh sách sách nhóm tàu"
      :visible.sync="vesselGroup.isShowvesselGroupGrid"
      width="50%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editUserForm"
        :model="editUser"
        label-position="left"
        label-width="180px"
      >
        <el-date-picker
          v-model="vesselGroupQuery.startTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false"
          style="width: 200px"
          type="datetime"
          placeholder="Từ ngày"
        ></el-date-picker>
        <el-date-picker
          v-model="vesselGroupQuery.endTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false"
          style="width: 200px; margin-left: 10px"
          type="datetime"
          placeholder="Đến ngày"
        ></el-date-picker>
      </el-form>
      <br />
      <el-table
        id="my-table"
        v-loading="loading"
        :data="lstVesselGroup"
        border
        fit
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column align="center" label="#" width="50">
          <template slot-scope="scope">
            <span>{{
              (vesselGroupQuery.currentPage - 1) *
                vesselGroupQuery.rowsPerPage +
              scope.$index +
              1
            }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="ID" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.id }}</span>
          </template>
        </el-table-column> -->

        <el-table-column label="Tên nhóm tàu">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Ghi chú">
          <template slot-scope="scope">
            <span>{{ scope.row.note }}</span>
          </template>
        </el-table-column>

        <el-table-column label="Tác động" width="150">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleGetVoyageDetail(scope)"
              >Chi tiết hành trình</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="vesselGroupQuery.total"
        :page.sync="vesselGroupQuery.currentPage"
        :limit.sync="vesselGroupQuery.rowsPerPage"
        :hidden="false"
        @pagination="getListVesselGroup"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="isShowDialog = false">Bỏ qua</el-button>
        <el-button type="primary" @click="handleGetVesselGroup()"
          >Tìm kiếm</el-button
        >
      </div>
    </el-dialog>

    <el-dialog
      v-dialogDrag
      id="dialogListArea"
      top="5vh"
      title="Tìm kiếm danh sách vùng"
      :visible.sync="vung.isShowDialogQuery"
      width="100%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="getListAreaForm"
        :model="editUser"
        label-position="left"
        label-width="180px"
      >
        <el-date-picker
          v-model="areaQuery.startTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          :editable="false"
          style="width: 200px"
          type="datetime"
          placeholder="Từ ngày"
        ></el-date-picker>
        <el-date-picker
          v-model="areaQuery.endTime"
          size="small"
          format="dd/MM/yyyy HH:mm:ss"
          :editable="false"
          style="width: 200px; margin-left: 10px"
          type="datetime"
          placeholder="Đến ngày"
        ></el-date-picker>
      </el-form>
      <br />
      <el-table
        id="my-table"
        v-loading="loading"
        :data="lstAreaShow"
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
        <el-table-column label="id" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Tên vùng">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Tạo bởi">
          <template slot-scope="scope">
            <span>{{ scope.row.createdBy }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="Ngày tạo">
          <template slot-scope="scope">
            <span>{{ scope.row.createdTime }}</span>
          </template>
        </el-table-column> -->
        <el-table-column label="Tác động" width="150">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="small"
              @click="editAreaRemote(scope.row)"
              >Sửa</el-button
            >
            <el-button
              type="primary"
              size="small"
              @click="deleteAreaRemote(scope.row)"
              >Xóa</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="areaQuery.total"
        :page.sync="areaQuery.currentPage"
        :limit.sync="areaQuery.rowsPerPage"
        :hidden="areaQuery.total < areaQuery.rowsPerPage"
        @pagination="getListArea"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="vung.isShowDialogQuery = false">Bỏ qua</el-button>
        <el-button type="primary" @click="handleGetListArea()"
          >Tìm kiếm</el-button
        >
      </div>
    </el-dialog>
    <el-dialog
      v-dialogDrag
      id="dialogDrawArea"
      top="5vh"
      title="Vùng"
      @close="cancelDrawArea()"
      :visible.sync="isShowVung"
      width="100%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="addAreaForm"
        :model="vung"
        label-position="left"
        label-width="80px"
      >
        <el-form-item prop="areaName" label="Tên vùng">
          <el-input
            v-model="vung.name"
            name="areaName"
            type="text"
            auto-complete="on"
            placeholder="Tên vùng"
          />
        </el-form-item>
        <el-form-item prop="areaType" label="Loại vùng">
          <el-select id="type" v-model="vung.vungType" @change="vungTypeChange">
            <el-option value="1" :label="'Point'">Point</el-option>
            <el-option value="2" :label="'LineString'">LineString</el-option>
            <el-option value="3" :label="'Polygon'">Polygon</el-option>
            <!-- <el-option value="Circle">Circle</el-option> -->
          </el-select>
        </el-form-item>
        <el-form-item prop="areaValue" label="Value">
          <textarea
            v-model="vung.value"
            name="areaName"
            disabled
            type="text"
            rows="4"
            auto-complete="on"
            placeholder="Value"
            style="width: 100%; resize: none"
          ></textarea>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelDrawArea()">Bỏ qua</el-button>
        <el-button type="primary" @click="saveDraw()">Lưu vùng</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Map from "ol/Map.js";
import View from "ol/View.js";

import ImageWMS from "ol/source/ImageWMS";
import { OSM, Vector as VectorSource } from "ol/source";
import { fromLonLat, toLonLat } from "ol/proj";
import VectorLayer from "ol/layer/Vector";
import { Image as ImageLayer } from "ol/layer";
import { Tile as TileLayer, Vector } from "ol/layer";
import MousePosition from "ol/control/MousePosition";
import { createStringXY } from "ol/coordinate";
import { GeoJSON, WKT } from "ol/format";
import XYZ from "ol/source/XYZ";
import Point from "ol/geom/Point";
import TileWMS from "ol/source/TileWMS";
import { Draw, Modify, Snap, defaults, Translate } from "ol/interaction";
import { LineString } from "ol/geom";
import Feature from "ol/Feature";
import Circle from "ol/geom/Circle";
import { Cluster, Stamen } from "ol/source";
import OverallApi from "../api/overall/api";
import { toStringHDMS } from "ol/coordinate";
import { defaults as defaultControls } from "ol/control";
import $ from "jquery";
import Overlay from "ol/Overlay";
const dataSourceResource = new Resource("manager/source/get-all");
import {
  Circle as CircleStyle,
  Icon,
  Style,
  Stroke,
  RegularShape,
  Fill,
  Text,
} from "ol/style";
import { Select, defaults as defaultInteractions } from "ol/interaction";
import KML from "ol/format/KML";
import { createEmpty, extend, getHeight, getWidth } from "ol/extent";
import Pagination from "@/components/Pagination";
import Resource from "@/api/resource";
const countriesResource = new Resource("ais/country");
const vesselTypeResource = new Resource("ais/vessel-type");
const hanoi = [105.804817, 21.028511];
let map;
let source;
let currentDraw;

let today = new Date();
let day = checkZero(today.getDate() + "");
let month = checkZero(today.getMonth() + 1 + "");

const hanoiWebMercator = fromLonLat(hanoi);

var currentDrawSource = new VectorSource({ wrapX: false });

var earthquakeFill = new Fill({
  color: "rgba(255, 153, 0, 0.8)",
});
var earthquakeStroke = new Stroke({
  color: "rgba(255, 204, 0, 0.2)",
  width: 1,
});

var textFill = new Fill({
  color: "#fff",
});
var textStroke = new Stroke({
  color: "rgba(0, 0, 0, 0.6)",
  width: 3,
});
var invisibleFill = new Fill({
  color: "rgba(255, 255, 255, 0.01)",
});

var mousePositionControl = new MousePosition({
  coordinateFormat: createStringXY(4),
  projection: "EPSG:4326",
  // comment the following two lines to have the mouse position
  // be placed within the map.
  className: "custom-mouse-position",
  target: document.getElementById("mouse-position"),
  undefinedHTML: "&nbsp;",
});

function createStyle(src, img) {
  return new Style({
    image: new Icon({
      anchor: [0.5, 0.96],
      crossOrigin: "anonymous",
      src: src,
      img: img,
      imgSize: [50, 50],
    }),
  });
}

function selectStyleFunction(feature) {
  if (currentDraw != null) {
    return;
  }
  var styles = [
    new Style({
      image: new CircleStyle({
        radius: feature.get("radius"),
        fill: new Fill({
          color: "rgba(255, 255, 255, 0.01)",
        }),
      }),
    }),
  ];
  var originalFeatures = feature.get("features");
  var originalFeature;
  for (var i = originalFeatures.length - 1; i >= 0; --i) {
    originalFeature = originalFeatures[i];
    styles.push(createEarthquakeStyle(originalFeature));
  }
  return styles;
}

var styleFunction = function (feature) {
  var geometry = feature.getGeometry();
  var styles = [
    // linestring
    new Style({
      stroke: new Stroke({
        color: "red",
        width: 2,
      }),
    }),
  ];
  // geometry.forEachSegment(function (start, end) {
  //   var dx = end[0] - start[0];
  //   var dy = end[1] - start[1];
  //
  //   var rotation = Math.atan2(dy, dx);
  //   // arrows
  //   styles.push(
  //     new Style({
  //       geometry: new Point(end),
  //       image: new Icon({
  //         src:
  //           "http://192.168.61.106:9985/vsat/static/img/vesselTypes/Yacht.png",
  //         anchor: [0.5, 0.5],

  //         scale: 1.2,
  //         rotateWithView: true,
  //         rotation: -rotation-180,
  //       }),
  //     })
  //   );
  //   styles.push(
  //     new Style({
  //       geometry: new Point(start),
  //       image: new Icon({
  //         src:
  //           "http://192.168.61.106:9985/vsat/static/img/vesselTypes/Yacht.png",
  //         anchor: [0.5, 0.5],

  //         scale: 1.2,
  //         rotateWithView: true,
  //         rotation: -rotation-180,
  //       }),
  //     })
  //   );
  // });

  return styles;
};

var iconVessel;

function createEarthquakeStyle(feature) {
  // 2012_Earthquakes_Mag5.kml stores the magnitude of each earthquake in a
  // standards-violating <magnitude> tag in each Placemark.  We extract it
  // from the Placemark's name instead.
  // var name = feature.get("name");
  // var magnitude = parseFloat(name.substr(2));
  // var radius = 5 + 20 * (magnitude - 5);
  // console.log("direction: ", feature.values_.data.sog);
  iconVessel = new Icon({
    src: "http://192.168.61.106:9985/vsat/static/img/vesselTypes/Yacht.png",
    anchor: [0.5, 0.5],
    scale: 1.3,
    rotateWithView: true,
    rotation: -90 + feature.values_.data.cog,
  });
  return new Style({
    geometry: feature.getGeometry(),
    image: iconVessel,
  });
}

var maxFeatureCount;
var vector = null;
var calculateClusterInfo = function (resolution) {
  maxFeatureCount = 0;
  var features = vector.getSource().getFeatures();
  var feature, radius;
  for (var i = features.length - 1; i >= 0; --i) {
    feature = features[i];
    var originalFeatures = feature.get("features");
    var extent = createEmpty();
    var j = void 0,
      jj = void 0;
    for (j = 0, jj = originalFeatures.length; j < jj; ++j) {
      extend(extent, originalFeatures[j].getGeometry().getExtent());
    }
    maxFeatureCount = Math.max(maxFeatureCount, jj);
    radius = (0.25 * (getWidth(extent) + getHeight(extent))) / resolution;
    feature.set("radius", radius);
  }
};

var currentResolution;
function styleFunctionClutter(feature, resolution) {
  if (resolution != currentResolution) {
    calculateClusterInfo(resolution);
    currentResolution = resolution;
  }
  var style;
  var size = feature.get("features").length;
  if (size > 1) {
    style = new Style({
      image: new CircleStyle({
        radius: feature.get("radius"),
        fill: new Fill({
          color: [255, 153, 0, Math.min(0.8, 0.4 + size / maxFeatureCount)],
        }),
      }),
      text: new Text({
        text: size.toString(),
        fill: textFill,
        stroke: textStroke,
      }),
    });
  } else {
    var originalFeature = feature.get("features")[0];
    style = createEarthquakeStyle(originalFeature);
  }
  return style;
}

function convertCoordinates(lon, lat) {
  return [lon, lat];

  // var x = (lon * 20037508.34) / 180;
  // var y = Math.log(Math.tan(((90 + lat) * Math.PI) / 360)) / (Math.PI / 180);
  // y = (y * 20037508.34) / 180;
  // return [x, y];
}

var overLayPopup = null;
var feature_onClick;
export default {
  name: "map-openlayers",
  components: {
    Pagination,
  },
  data() {
    return {
      translate: null,
      positionInfo: {},
      listAIS: [],
      listAISShow: [],
      loading: false,
      voyage: {
        isShowShipGrid: false,
      },
      vesselGroup: {
        isShowvesselGroupGrid: false,
      },
      lstVesselFeature: [],
      areaQuery: {
        startTime: null,
        endTime: null,
        perPage: 5,
        currentPage: 1,
        rowsPerPage: 5,
        vesselGroup: "",
        total: 0,
        sort: "createdAt",
      },
      listCountry: [],
      listCountrySearch: [
        {
          id: null,
          name: null,
        },
      ],
      listVesselType: [],
      list_vesselTypeSearch: [
        {
          id: null,
          name: null,
        },
      ],
      query: {
        isAdvance: false,
        sourceIps: null,
        destIps: null,
        mmsi: null,
        mobile: null,
        startTime: today.getFullYear() + "-" + month + "-" + day + " 00:00:00",
        endTime: getCurrentDateToStr(today),
        perPage: 5,
        currentPage: 1,
        rowsPerPage: 5,
        vesselGroup: "",
        area: null,
        vesselType: null,
        country: null,
        total: 0,
        sort: "createdAt",
        dataSource: [],
      },
      lstDataSource: [],
      lstVesselGroup: [],
      lstArea: [],
      lstAreaShow: [],
      vesselGroupQuery: {
        startTime: null,
        endTime: null,
        currentPage: 1,
        rowsPerPage: 5,
        total: 0,
        sort: "createdAt",
      },
      featureID: 0,
      singleClick: null,
      selectedFeatureID: null,

      isShowDialog: false,
      isShowVung: false,
      editUser: {},
      vung: {
        value: null,
        areaUpdate: null,
        isShowDialogQuery: true,
        vungType: "Polygon",
        isDrawing: false,
        name: "",
      },
      vesselDetailDialog: {
        vesselName: "T",
      },
      isShowVesselDetailDialog: true,
    };
  },
  created() {
    console.log("Created");
  },
  mounted() {
    console.log("param query: ", this.$route.query);
    source = new VectorSource({ wrapX: false });

    var vector = new VectorLayer({
      source: source,
    });
    console.log("mounted", map);
    if (map != null) {
      map = null;
    }

    this.$nextTick(function () {
      setTimeout(() => {
        this.initiateMap();
      }, 1000);
    });

    this.init();
  },
  methods: {
    init() {
      // this.handleGetVesselGroup(1, 10000000);
      this.handleGetListArea();
      // this.getListVesselType();
      // this.getListCountries();
      // this.getAllDataSource();
      // this.handleClutersMake();
    },
    initiateMap() {
      console.log("init map... ", map);

      var source = new VectorSource();
      // var vector = new VectorLayer({
      //   source: source,
      // });
      // create title layer
      var raster = new TileLayer({
        source: new OSM(),
      });

      var vector = new VectorLayer({
        source: currentDrawSource,
      });

      console.log("new map... ", map);

      // create map with 2 layer
      map = new Map({
        target: "mapOL",
        controls: defaultControls().extend([mousePositionControl]),
        // interactions: defaultInteractions().extend([
        //   new Select({
        //     condition: function (evt) {
        //       return evt.type == "pointermove" || evt.type == "singleclick";
        //     },
        //     style: selectStyleFunction,
        //   }),
        // ]),
        layers: [
          new TileLayer({
            source: new TileWMS({
              url: "http://192.168.61.106:3691/geoserver/wms",
              params: {
                LAYERS: "EyeseaDelta",
                TILED: true,
                VERSION: "1.1.1",
                FORMAT: "image/jpeg",
                EPSG: 4326,
              },
              serverType: "geoserver",
            }),
          }),
          vector,
        ],
        view: new View({
          projection: "EPSG:4326",
          center: hanoi,
          zoom: 1,
        }),
      });
      overLayPopup = new Overlay({
        element: document.getElementById("popup"),
        offset: [350, 0],
      });

      map.on("click", async (evt) => {
        console.log("map click");
        if (currentDraw != null || this.vung.areaUpdate) {
          return;
        }

        var element = overLayPopup.getElement();
        var coordinate = evt.coordinate;
        var hdms = toStringHDMS(toLonLat(coordinate));

        overLayPopup.setPosition(coordinate);

        feature_onClick = map.forEachFeatureAtPixel(
          evt.pixel,
          function (feature, layer) {
            console.log(feature);
            return feature;
          }
        );

        if (feature_onClick) {
          // var content = document.getElementById("popup-content");
          // console.log(feature_onClick.getProperties().name);
          // overLayPopup.setPosition(evt.coordinate);
          this.isShowVesselDetailDialog = true;
          if (
            feature_onClick.values_.features &&
            feature_onClick.values_.features.length == 1
          ) {
            var data = feature_onClick.values_.features[0].values_.data;
            console.log(data);

            const overallApi = new OverallApi();
            var res = await overallApi.getVesselDetail(
              "/ais/vessel/detail/" + data.objId
            );
            if (res.status == 200) {
              var vesselDetailInfo = res.data != null ? res.data : {};

              this.vesselDetailDialog = vesselDetailInfo;
            }
            this.vesselDetailDialog = { ...data, ...this.vesselDetailDialog };
            console.log("detail vessel ", this.vesselDetailDialog);
            // document.getElementById("popup-content").innerHTML =
            //   "Mã tàu " +
            //   data.objId +
            //   "</br>" +
            //   "Tên Tàu " +
            //   vesselDetailInfo.vesselName +
            //   "</br>" +
            //   0;
            // "Kinh độ: " +
            //   data.longitude +
            //   "</br>" +
            //   "Vĩ độ " +
            //   data.latitude +
            //   "</br>";
          } else if (feature_onClick.values_.data) {
            var data = feature_onClick.values_.data;
            console.log(data);
            const overallApi = new OverallApi();
            var res = await overallApi.getVesselDetail(
              "/ais/vessel/detail/" + data.objId
            );
            if (res.status == 200) {
              var vesselDetailInfo = res.data != null ? res.data : {};
              console.log("detail vessel ", vesselDetailInfo);
              this.vesselDetailDialog = vesselDetailInfo;
            }
            this.vesselDetailDialog = { ...data, ...this.vesselDetailDialog };
            console.log("detail vessel ", this.vesselDetailDialog);
          } else if (
            feature_onClick.type == "area" ||
            feature_onClick.values_.type == "area"
          ) {
            var data = feature_onClick.data
              ? feature_onClick.data
              : feature_onClick.values_.dataVung;
            console.log(data);
            document.getElementById("popup-content").innerHTML =
              "vùng: " + data.name + "</br>";
          } else {
            overLayPopup.setPosition(null);
            this.isShowVesselDetailDialog = false;
            this.positionInfo.longitude = coordinate[0];
            this.positionInfo.latitude = coordinate[1];
          }
        } else {
          overLayPopup.setPosition(null);
          this.isShowVesselDetailDialog = false;
          this.positionInfo.longitude = coordinate[0];
          this.positionInfo.latitude = coordinate[1];
        }
      });

      map.addOverlay(overLayPopup);
    },

    async getAllDataSource() {
      try {
        var res = await dataSourceResource.list();
        if (res.status == 200) {
          this.lstDataSource = res.data;
        }
        console.log("data source ", res);
      } catch (error) {
        console.error("data source ", error);
      }

      // if (data) this.dataSourceLst = data;
    },

    async getListVesselType() {
      vesselTypeResource
        .list()
        .then((response) => {
          if (response.status === 200) {
            this.listVesselType = response.data;
            this.list_vesselTypeSearch = response.data;
            this.vesselTypeInfo = {
              id: response.data[0].id,
              name: response.data[0].typeName,
            };
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async getListCountries() {
      countriesResource
        .list()
        .then((response) => {
          if (response.status === 200) {
            this.listCountry = response.data;
            this.listCountrySearch = response.data;
            this.list_placeOfBuild = response.data;

            this.countryInfo = {
              id: response.data[0].id,
              name: response.data[0].name,
            };

            this.placeOfBuildInfo = {
              id: response.data[0].id,
              name: response.data[0].name,
            };
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },

    async handleGetVoyageDetail(data) {
      this.clearAllFeature();
      console.log("GetVoyageDetail", data);
      const overallApi = new OverallApi();
      var resListAISDetail = await overallApi.searchListAis(
        "/ais/search-list",
        {
          startTime: this.query.startTime,
          endTime: this.query.endTime,
          mmsi: data.row ? data.row.objId : data.objId,
          limit: 10000,
        }
      );

      console.log("result VoyageDetail", resListAISDetail);
      if (resListAISDetail.status == 200) {
        console.log("result VoyageDetail", resListAISDetail.data);
        if (resListAISDetail.data && resListAISDetail.data.length < 2) {
          this.$notify({
            title: "Chỉ có 1 ví trí tàu được tìm thấy",
           
            type: "waring",
          });
        }
        this.handleLineStringDraw(resListAISDetail.data);
      }
      if (resListAISDetail.data && resListAISDetail.data.length > 0) {
        this.centerToPoisition(
          resListAISDetail.data[0].longitude,
          resListAISDetail.data[0].latitude
        );
      }

      this.isShowDialog = false;
    },

    handleChangeVesselGroupSelect() {
      console.log("vessel group change");
    },

    //phân trang list nhóm tàu
    getListVesselGroup(obj) {
      console.log("opaging: ", obj);
      this.listAISShow = this.listAIS.slice(
        obj.limit * (obj.page - 1),
        obj.limit * obj.page
      );
      this.handleGetVesselGroup(obj.page, obj.limit);
    },

    openDialogLoadListVesselGroup() {
      this.vesselGroup.isShowvesselGroupGrid = true;
    },

    async handleGetVesselGroup(currentpage, rowsPerPage) {
      console.log("GetVesselGroup");
      const overallApi = new OverallApi();
      var resListVesselGroup = await overallApi.searchVesselGroup(
        "/ais/object-group/search-list",
        {
          startTime: this.vesselGroupQuery.startTime,
          endTime: this.vesselGroupQuery.endTime,
          currentPage: currentpage
            ? currentpage
            : this.vesselGroupQuery.currentPage,
          rowsPerPage: rowsPerPage
            ? rowsPerPage
            : this.vesselGroupQuery.rowsPerPage,
        }
      );
      if (resListVesselGroup.status == 200) {
        this.lstVesselGroup = resListVesselGroup.data;
        this.vesselGroupQuery.total = resListVesselGroup.total;
      }

      console.log(resListVesselGroup);
    },

    async handleGetListArea() {
      console.log("handleGetListArea");
      const overallApi = new OverallApi();

      var resListArea = await overallApi.searchVesselGroup(
        "/map/get-list-area",
        {
          startTime: this.areaQuery.startTime ? this.areaQuery.startTime : null,
          endTime: this.areaQuery.endTime ? this.areaQuery.endTime : null,
        }
      );
      if (resListArea.status == 200) {
        this.lstArea = resListArea.data;
        this.areaQuery.total = resListArea.data ? resListArea.data.length : 0;
        if (resListArea.data == null || resListArea.data.length == 0) {
          this.lstAreaShow = [];
          this.areaQuery.total = 0;
          return;
        }
        // this.$notify({
        //   message: "Tìm thấy " + this.lstArea.length + " Vùng.",
        //   // position: "top right",
        //   type: "success",
        // });
        this.lstAreaShow = this.lstArea.slice(
          this.areaQuery.rowsPerPage * (this.areaQuery.currentPage - 1),
          this.areaQuery.rowsPerPage * this.areaQuery.currentPage
        );
        this.areaQuery.total = this.lstArea.length;
      }

      console.log(resListArea);
    },

    getListAIS(obj) {
      console.log(obj);
      this.listAISShow = this.listAIS.slice(
        obj.limit * (obj.page - 1),
        obj.limit * obj.page
      );
    },

    getListArea(obj) {
      console.log(obj);
      this.lstAreaShow = this.lstArea.slice(
        obj.limit * (obj.page - 1),
        obj.limit * obj.page
      );
    },

    editAreaRemote(area) {
      console.log("Edit area", area);
      this.clearAllFeature();
      this.vung.isShowDialogQuery = false;
      this.isShowVung = true;
      this.vung.areaUpdate = area;
      this.vung.name = area.name;

      this.vung.vungType = area.type == 0 ? "3" : "" + area.type + "";
      this.vung.value = area.value;
      this.vung.id = area.id;
      //draw area
      var wkt = area.value;
      // "POLYGON((126.84244527822119 18.77993158494867,126.84244527822119 18.77993158494867,126.43195497035344 18.77993158494867,126.2267098164196 18.77993158494867,126.02146466248573 18.77993158494867,125.40572920068414 19.190421892816403,125.20048404675026 19.190421892816403,124.78999373888254 19.395667046750262,124.58474858494867 19.395667046750262,124.58474858494867 19.395667046750262,123.96901312314708 19.600912200684135,123.7637679692132 19.600912200684135,123.14803250741161 19.600912200684135,122.73754219954387 19.600912200684135,122.73754219954387 19.600912200684135,122.12180673774228 19.600912200684135,122.12180673774228 19.600912200684135,121.50607127594068 19.600912200684135,121.50607127594068 19.600912200684135,121.09558096807297 19.600912200684135,120.89033581413909 19.600912200684135,120.89033581413909 19.600912200684135,120.2746003523375 19.600912200684135,120.06935519840363 19.600912200684135,119.65886489053591 19.600912200684135,119.45361973660204 19.600912200684135,119.04312942873429 19.395667046750262,118.63263912086657 19.190421892816403,118.22214881299885 19.190421892816403,118.01690365906498 18.985176738882544,117.8116585051311 18.77993158494867,117.8116585051311 18.16419612314708,117.8116585051311 17.958950969213205,117.8116585051311 17.958950969213205,117.8116585051311 17.548460661345473,117.8116585051311 17.548460661345473,117.8116585051311 17.137970353477755,117.8116585051311 16.93272519954388,117.8116585051311 16.52223489167615,117.8116585051311 16.52223489167615,117.8116585051311 16.111744583808417,117.8116585051311 15.701254275940698,118.01690365906498 15.496009122006825,118.01690365906498 15.085518814139093,118.01690365906498 15.085518814139093,118.01690365906498 14.67502850627136,118.01690365906498 14.67502850627136,118.22214881299885 14.264538198403628,118.22214881299885 14.264538198403628,118.22214881299885 13.85404789053591,118.22214881299885 13.648802736602036,118.4273939669327 13.443557582668177,118.63263912086657 13.238312428734304,118.63263912086657 13.238312428734304,118.63263912086657 12.827822120866571,118.63263912086657 12.827822120866571,118.83788427480044 12.417331812998839,118.83788427480044 12.417331812998839,118.83788427480044 12.00684150513112,118.83788427480044 12.00684150513112,119.04312942873429 11.596351197263388,119.04312942873429 11.185860889395656,119.24837458266816 10.57012542759405,119.24837458266816 10.57012542759405,119.45361973660204 10.36488027366019,119.65886489053591 9.954389965792458,119.65886489053591 9.954389965792458,120.06935519840363 9.338654503990867,120.06935519840363 9.133409350056994,120.2746003523375 8.928164196123134,120.2746003523375 8.722919042189261,120.47984550627137 8.517673888255402,120.68509066020522 8.312428734321543,120.89033581413909 8.10718358038767,120.89033581413909 8.10718358038767,121.30082612200681 7.491448118586078,121.50607127594068 7.286202964652205,121.71131642987456 7.286202964652205,121.71131642987456 7.286202964652205,121.91656158380843 7.0809578107183455,121.91656158380843 6.875712656784472,122.32705189167615 6.670467502850613,122.53229704561002 6.465222348916754,122.73754219954387 6.259977194982881,123.14803250741161 6.259977194982881,123.35327766134549 6.259977194982881,123.96901312314708 5.849486887115148,124.3795034310148 5.644241733181289,124.78999373888254 5.438996579247416,125.40572920068414 5.233751425313557,125.81621950855185 5.028506271379683,125.81621950855185 5.028506271379683,126.2267098164196 4.823261117445824,126.2267098164196 4.823261117445824,126.63720012428732 4.823261117445824,126.63720012428732 4.823261117445824,127.04769043215506 4.823261117445824,127.04769043215506 4.823261117445824,127.45818074002278 4.823261117445824,127.66342589395666 4.823261117445824,127.66342589395666 4.823261117445824,128.07391620182437 4.823261117445824,128.48440650969212 4.823261117445824,128.68965166362597 5.028506271379683,129.1001419714937 5.233751425313557,129.1001419714937 5.233751425313557,129.51063227936143 5.233751425313557,129.51063227936143 5.233751425313557,129.92112258722918 5.233751425313557,130.12636774116302 5.438996579247416,130.53685804903077 5.438996579247416,130.53685804903077 5.644241733181289,130.74210320296464 5.849486887115148,131.15259351083236 6.0547320410490215,131.35783866476623 6.259977194982881,131.35783866476623 6.259977194982881,131.76832897263395 6.670467502850613,131.97357412656783 7.0809578107183455,132.1788192805017 7.286202964652205,132.38406443443554 7.491448118586078,132.58930958836942 7.696693272519937,132.58930958836942 7.696693272519937,132.7945547423033 8.312428734321543,132.99979989623716 8.517673888255402,133.205045050171 8.722919042189261,133.41029020410488 9.133409350056994,133.41029020410488 9.338654503990867,133.41029020410488 9.338654503990867,133.41029020410488 9.954389965792458,133.41029020410488 9.954389965792458,133.41029020410488 10.36488027366019,133.41029020410488 10.775370581527923,133.41029020410488 10.775370581527923,133.41029020410488 11.185860889395656,133.41029020410488 11.596351197263388,133.41029020410488 11.596351197263388,133.41029020410488 12.00684150513112,133.41029020410488 12.00684150513112,133.41029020410488 12.417331812998839,133.41029020410488 12.827822120866571,133.41029020410488 13.033067274800445,133.205045050171 13.238312428734304,132.99979989623716 13.648802736602036,132.7945547423033 14.059293044469769,132.58930958836942 14.469783352337501,132.58930958836942 14.469783352337501,132.38406443443554 14.67502850627136,132.38406443443554 14.67502850627136,132.1788192805017 15.085518814139093,131.97357412656783 15.085518814139093,131.76832897263395 15.290763968072966,131.35783866476623 15.701254275940698,131.15259351083236 16.111744583808417,131.15259351083236 16.111744583808417,130.74210320296464 16.52223489167615,130.3316128950969 16.93272519954388,130.12636774116302 17.137970353477755,130.12636774116302 17.137970353477755,129.92112258722918 17.548460661345473,129.7158774332953 17.548460661345473,129.51063227936143 17.753705815279346,129.51063227936143 17.753705815279346,128.89489681755984 18.16419612314708,128.89489681755984 18.16419612314708,128.48440650969212 18.16419612314708,128.07391620182437 18.16419612314708,127.86867104789053 18.369441277080938,127.45818074002278 18.57468643101481,127.45818074002278 18.57468643101481,126.84244527822119 18.77993158494867,126.84244527822119 18.77993158494867,126.43195497035344 18.77993158494867,126.84244527822119 18.77993158494867))";

      var format = new WKT();

      var feature = format.readFeature(wkt, {
        dataProjection: "EPSG:4326",
        featureProjection: "EPSG:4326",
      });
      feature.data = area;
      // feature.type = "area";

      var areaLayer = new VectorLayer({
        source: new VectorSource({
          features: [feature],
        }),
      });
      map.addLayer(areaLayer);
      this.zoomAndselected(feature);
      // this.handleDrawVung();
      this.addSelect();
      var snap = new Snap({ source: currentDrawSource });
      map.addInteraction(snap);
      this.addTranslate();
    },

    cancelDrawArea() {
      this.isShowVung = false;
      this.vung.isDrawing = false;
      this.clearAllFeature();
      this.removeSelect();
      this.removeTranslate();
    },

    closerPopup() {
      overLayPopup.setPosition(undefined);
      return false;
    },

    handleDraw() {
      currentDraw = new Draw({
        source: currentDrawSource,
        type: "LineString",
      });
      map.addInteraction(currentDraw);
    },
    removeHanleDraw() {
      if (currentDraw) {
        map.removeInteraction(currentDraw);
      }
    },
    handleEndDraw() {
      try {
        this.addSelect();
        this.vung.isDrawing = false;
        map.removeInteraction(currentDraw);
        // var features = currentDraw.source_.getFeatures();
        // var newForm = new WKT();
        // // new WKT().readFeature(linestringWKT);
        // var arrWKTFeature = [];
        // for (var i = 0; i < features.length; i++) {
        //   var featColl = newForm.writeGeometry(features[i].getGeometry());
        //   arrWKTFeature.push(featColl);
        // }
        // console.log("data vung: ", arrWKTFeature);

        this.addTranslate();
        console.log("add translate feature");
      } catch (error) {
        console.log("end draw error ", error.message);
      }
    },

    addTranslate() {
      this.translate = new Translate({
        features: this.singleClick.getFeatures(),
      });
      this.translate.on("translateend", (evt) => {
        evt.features.forEach((feat) => {
          console.log("translateend: ", feat);
          this.vung.value = this.getValueAreaDraw(feat);
          console.log("value: ", this.areaQuery.value);
        });
      });
      map.addInteraction(this.translate);
    },
    removeTranslate() {
      map.removeInteraction(this.translate);
    },

    getValueAreaDraw(feature) {
      var newForm = new WKT();
      var wktValue = newForm.writeGeometry(feature.getGeometry());
      return wktValue;
    },

    openListAreaDialog() {
      this.handleGetListArea();
      this.vung.isShowDialogQuery = true;
    },

    async saveDraw() {
      try {
        if (this.vung.value == null || this.vung.value == "") {
          this.$notify({
            title: "Lỗi",
            message: "Giá trị bản đồ rỗng",

            type: "error",
          });
          return;
        } else if (this.vung.name == null || this.vung.name == "") {
          this.$notify({
            title: "Lỗi",
            message: "Tên vùng không được để trống",
  
            type: "error",
          });
          return;
        }
        // new WKT().readFeature(linestringWKT);
        var arrWKTFeature = [];
        var area = {};
        area.name = this.vung.name;
        area.value = this.vung.value;
        console.log("vung type ", this.vung.vungType);
        switch (this.vung.vungType) {
          case "1":
            area.type = 1;
            break;

          case "2":
            area.type = 2;
            break;

          case "3":
            area.type = 3;
            break;
          default:
            area.type = 3;
            break;
        }

        area.id = this.vung.id;
        console.log("area: ", area);
        arrWKTFeature.push(area);
        console.log("save draw ", arrWKTFeature);
        const overallApi = new OverallApi();
        var res = await overallApi.saveDraw("/map/save-area", {
          listAddAreaRequest: arrWKTFeature,
        });
        console.log("data vung: ", arrWKTFeature);
        if (res.status == 200 && (res.message == null || res.message == "")) {
          this.$notify({
            title: "Lưu vùng thành công",

            type: "success",
          });
          currentDraw = null;
          this.cancelDrawArea();
          this.removeSelect();
          this.removeTranslate();
          this.openListAreaDialog();
        } else {
          this.$notify({
            title: "Lưu vùng thất bại",
            message: res.message,

            type: "error",
          });
        }
      } catch (error) {
        this.$notify({
          title: "Lưu vùng thất bại",
          
          type: "error",
        });
        console.log("save  draw error ", error);
      }
    },

    async deleteAreaRemote(area) {
      this.$confirm("Bạn chắc chắn muốn xóa vùng này?", "Cảnh báo", {
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Hủy bỏ",
        type: "warning",
      })
        .then(async () => {
          const overallApi = new OverallApi();
          var res = await overallApi.deleteArea("/map/del-area", {
            id: area.id,
          });
          if (res.status == 200 && (res.message == null || res.message == "")) {
            this.$notify({
              title: "Xóa vùng thành công",
            
              type: "success",
            });
            this.handleGetListArea();
            this.clearAllFeature();
          } else {
            this.$notify({
              title: "Xóa vùng thất bại",
              message: res.message,
             
              type: "error",
            });
          }
        })
        .catch(() => {
          console.log("cancel delete.");
        });
    },

    editArea() {
      this.isShowVung = true;
      this.vung.isUpdate = true;
      console.log("edit area ", this.selectedFeature);
      this.vung = this.selectedFeature;
    },

    addArea() {
      this.clearAllFeature();
      this.isShowVung = true;
      this.vung = {
        value: null,
        areaUpdate: null,
        isShowDialogQuery: this.vung.isShowDialogQuery,
        vungType: "Polygon",
        isDrawing: this.vung.isDrawing,
        name: "",
        isUpdate: false,
      };
      this.handleDrawVung();
    },
    openDialogLoadListVessel() {
      this.isShowDialog = true;
    },

    drawLineStringGroup(data) {
      if (data && data.length > 0) {
        if (data.length > 1000) {
          this.$notify({
            title: "Vượt quá số lượng cho phép vẽ hành trình",
        
            type: "error",
          });
          return;
        }
        for (var i = 0; i < data.length; i++) {
          var _itemI = data[i];
          var arrLineString = [];
          arrLineString.push(_itemI);
          for (var j = i + 1; j < data.length; j++) {
            var _itemJ = data[j];
            if (_itemI.objId == _itemJ.objId) {
              arrLineString.push(_itemJ);
            }
          }

          if (arrLineString.length >= 2) {
            this.handleLineStringDraw(arrLineString);
          }
        }
      }
    },

    handleLineStringDraw(data) {
      var geojsonObject = {
        type: "FeatureCollection",
        crs: {
          type: "name",
          properties: {
            name: "EPSG:3857",
          },
        },
        features: [
          {
            type: "Feature",
            geometry: {
              type: "LineString",
              coordinates: [],
            },
          },
        ],
      };
      var arrVessel = [];

      for (var i = 0; i < data.length; i++) {
        var coordinate = [data[i].longitude, data[i].latitude];
        geojsonObject.features[0].geometry.coordinates.push(coordinate);

        var vesselPoint = new Point(
          convertCoordinates(data[i].longitude, data[i].latitude)
        );
        // vesselPoint.rotate(0.5);
        arrVessel.push(
          new Feature({
            geometry: vesselPoint,
            data: data[i],
            label: "Tàu",
          })
        );
        // map.addLayer(vessel);
      }
      if (geojsonObject.features[0].geometry.coordinates.length == 1) {
        geojsonObject.features[0].geometry.coordinates.push(
          geojsonObject.features[0].geometry.coordinates[0]
        );
      }
      var vectorSource = new VectorSource({
        features: new GeoJSON().readFeatures(geojsonObject, {
          dataProjection: "EPSG:4326",
          // featureProjection: "EPSG:3857",
        }),
      });

      var vectorLayer = new VectorLayer({
        source: vectorSource,
        style: styleFunction,
      });

      var layerPointVessel = new VectorLayer({
        source: new VectorSource({
          features: arrVessel,
        }),

        style: function (feature) {
          return new Style({
            image: new Icon({
              anchor: [0.5, 0.5],
              scale: 1.2,
              src:
                "http://192.168.61.106:9985/vsat/static/img/vesselTypes/Yacht.png",
              rotation: -90 + feature.values_.data.cog,
              rotateWithView: true,
            }),
          });
        },
      });

      map.addLayer(vectorLayer);
      map.addLayer(layerPointVessel);
    },

    async handleClutersMake(drawLineStringGroup) {
      this.clearAllFeature();
      const overallApi = new OverallApi();
      var listAIS;
      try {
        console.log(this.query);
        if (this.query.startTime == null || this.query.endTime == null) {
          this.$notify({
            title: "Lỗi",
            message: "Thời gian là bắt buộc",
            
            type: "error",
          });
          return;
        }
        listAIS = await overallApi.searchListAis("/ais/search-list-general", {
          startTime: this.query.startTime,
          endTime: this.query.endTime,
          vesselGroupIds: this.query.vesselGroup
            ? "" + this.query.vesselGroup.id + ""
            : "",
          sourceIps: this.query.sourceIps,
          destIps: this.query.destIps,
          mmsi: this.query.mmsi,

          // startTime: "2021-03-24 07:35:00",
          // endTime: "2021-03-24 23:00:00",

          limit: 500000,
        });

        if (listAIS.status == 200) {
          if (listAIS.data == null || listAIS.data.length == 0) {
            this.listAISShow = [];
            this.query.total = 0;
            return;
          }
          // this.$notify({
          //   message: "Tìm thấy " + listAIS.data.length + " Tàu.",
          //   position: "top right",
          //   type: "success",
          // });
          this.listAIS = listAIS.data;
          this.listAISShow = this.listAIS.slice(0, this.query.rowsPerPage);
          this.query.total = listAIS.data.length;
          // this.isShowDialog = false;
          this.lstVesselFeature = new Array(listAIS.data.length);

          for (var i = 0; i < listAIS.data.length; ++i) {
            var data = listAIS.data[i];
            var coordinates = [data.longitude, data.latitude];
            // lstVesselFeature[i] = new Feature(new Point(coordinates));
            // var coordinates = [
            //   2 * e * Math.random() - e,
            //   2 * e * Math.random() - e,
            // ];
            var vesselPoint = new Point(
              convertCoordinates(data.longitude, data.latitude)
            );
            // vesselPoint.rotate(0.5)
            this.lstVesselFeature[i] = new Feature({
              geometry: vesselPoint,

              data: data,
              label: "Tàu",
            });
            // console.log("draw all line", data);
            // this.handleLineStringDraw(data);
          }
        } else {
          alert("Get list vessel error.");
          return;
        }
        if (!drawLineStringGroup) {
          vector = new VectorLayer({
            source: new Cluster({
              distance: 40,
              source: new VectorSource({
                // format: new GeoJSON(),
                features: this.lstVesselFeature,
                // url:
                //   "https://openlayers.org/en/latest/examples/data/kml/2012_Earthquakes_Mag5.kml",
                // format: new KML({
                //   extractStyles: false,
                // }),
              }),
            }),
            style: styleFunctionClutter,
          });
          map.addLayer(vector);
        } else {
          this.drawLineStringGroup(this.listAIS);
        }
      } catch (error) {
        console.log("get list ais fail: ", error.message);
      }
      // this.isShowDialog = false;
    },

    clearAllFeature() {
      try {
        const layers = [...map.getLayers().getArray()];

        console.log("clear all 1", layers);
        layers.splice(0, 1);
        console.log("clear all 2", layers);
        layers.forEach((layer) => map.removeLayer(layer));

        map.removeInteraction(this.translate);
        map.removeInteraction(this.singleClick);
        map.removeInteraction(currentDraw);
        currentDrawSource = new VectorSource({ wrapX: false });
        map.addLayer(
          new VectorLayer({
            source: currentDrawSource,
          })
        );
        currentDraw = null;
      } catch (error) {
        console.log("error clear all feature");
      }
    },

    centerToPoisition(longitude, latitude, zoom) {
      // console.log("re center");
      var location = hanoi;
      if (longitude && latitude) {
        location = [longitude, latitude];
      }
      var view = new View({
        projection: "EPSG:4326",
        center: location,
        zoom: zoom ? zoom : map.getView().getZoom() + 5,
      });
      view.centerOn(location, map.getSize(), [
        map.getSize()[0] / 2 + 110,
        map.getSize()[1] / 2 + 160,
      ]);
      map.setView(view);
    },
    handleDrawVung() {
      // if (this.vung.name == "" || this.vung.name == null) {
      //   this.$notify({
      //     title: "Cảnh báo",
      //     message: "Tên vùng là bắt buộc",
      //     position: "top right",
      //     type: "info",
      //     position: "top-center",
      //   });
      // }

      // this.isShowVung = false;
      var value = "Polygon";
      switch (this.vung.vungType) {
        case "1": {
          value = "Point";
          break;
        }
        case "2": {
          value = "LineString";
          break;
        }
        case "3":
          {
            value = "Polygon";
            break;
          }
          defaults: {
            value = "Polygon";
            break;
          }
      }
      console.log("type ", value);
      if (value && value !== "None") {
        this.vung.isDrawing = true;
        currentDraw = new Draw({
          source: currentDrawSource,
          type: value,
          freehand: true,
        });
        currentDraw.on("drawend", (event) => {
          this.featureID = this.featureID + 1;
          console.log("drawend", this);
          event.feature.setProperties({
            id: this.featureID,
            name: "" + this.vung.name + "",
            dataVung: Object.assign({}, this.vung),
            type: "area",
          });
          this.handleEndDraw();
          this.vung.value = this.getValueAreaDraw(event.feature);
          console.log("value: ", this.areaQuery.value);
        });
        map.addInteraction(currentDraw);

        // var modify = new Modify({ source: currentDrawSource });
        // map.addInteraction(modify);

        var snap = new Snap({ source: currentDrawSource });
        map.addInteraction(snap);
      }
      this.addInteraction();
    },

    async handGetListAreaDraw() {
      try {
        // if (!this.areaQuery.startTime || !this.areaQuery.endTime) {
        //   this.$notify({
        //     title: "Lỗi",
        //     message: "StartTime và EndTime là bắt buộc",
        //     position: "top right",
        //     type: "error",
        //   });
        //   return;
        // }
        const overallApi = new OverallApi();
        var res = await overallApi.saveDraw("/map/get-list-area", {
          startTime: this.areaQuery.startTime,
          endTime: this.areaQuery.endTime,
        });
        if (res.status == 200) {
          this.vung.isShowDialogQuery = false;
          var arrFeature = [];
          for (var i = 0; i < res.data.length; i++) {
            try {
              var wkt = res.data[i].value;
              // "POLYGON((126.84244527822119 18.77993158494867,126.84244527822119 18.77993158494867,126.43195497035344 18.77993158494867,126.2267098164196 18.77993158494867,126.02146466248573 18.77993158494867,125.40572920068414 19.190421892816403,125.20048404675026 19.190421892816403,124.78999373888254 19.395667046750262,124.58474858494867 19.395667046750262,124.58474858494867 19.395667046750262,123.96901312314708 19.600912200684135,123.7637679692132 19.600912200684135,123.14803250741161 19.600912200684135,122.73754219954387 19.600912200684135,122.73754219954387 19.600912200684135,122.12180673774228 19.600912200684135,122.12180673774228 19.600912200684135,121.50607127594068 19.600912200684135,121.50607127594068 19.600912200684135,121.09558096807297 19.600912200684135,120.89033581413909 19.600912200684135,120.89033581413909 19.600912200684135,120.2746003523375 19.600912200684135,120.06935519840363 19.600912200684135,119.65886489053591 19.600912200684135,119.45361973660204 19.600912200684135,119.04312942873429 19.395667046750262,118.63263912086657 19.190421892816403,118.22214881299885 19.190421892816403,118.01690365906498 18.985176738882544,117.8116585051311 18.77993158494867,117.8116585051311 18.16419612314708,117.8116585051311 17.958950969213205,117.8116585051311 17.958950969213205,117.8116585051311 17.548460661345473,117.8116585051311 17.548460661345473,117.8116585051311 17.137970353477755,117.8116585051311 16.93272519954388,117.8116585051311 16.52223489167615,117.8116585051311 16.52223489167615,117.8116585051311 16.111744583808417,117.8116585051311 15.701254275940698,118.01690365906498 15.496009122006825,118.01690365906498 15.085518814139093,118.01690365906498 15.085518814139093,118.01690365906498 14.67502850627136,118.01690365906498 14.67502850627136,118.22214881299885 14.264538198403628,118.22214881299885 14.264538198403628,118.22214881299885 13.85404789053591,118.22214881299885 13.648802736602036,118.4273939669327 13.443557582668177,118.63263912086657 13.238312428734304,118.63263912086657 13.238312428734304,118.63263912086657 12.827822120866571,118.63263912086657 12.827822120866571,118.83788427480044 12.417331812998839,118.83788427480044 12.417331812998839,118.83788427480044 12.00684150513112,118.83788427480044 12.00684150513112,119.04312942873429 11.596351197263388,119.04312942873429 11.185860889395656,119.24837458266816 10.57012542759405,119.24837458266816 10.57012542759405,119.45361973660204 10.36488027366019,119.65886489053591 9.954389965792458,119.65886489053591 9.954389965792458,120.06935519840363 9.338654503990867,120.06935519840363 9.133409350056994,120.2746003523375 8.928164196123134,120.2746003523375 8.722919042189261,120.47984550627137 8.517673888255402,120.68509066020522 8.312428734321543,120.89033581413909 8.10718358038767,120.89033581413909 8.10718358038767,121.30082612200681 7.491448118586078,121.50607127594068 7.286202964652205,121.71131642987456 7.286202964652205,121.71131642987456 7.286202964652205,121.91656158380843 7.0809578107183455,121.91656158380843 6.875712656784472,122.32705189167615 6.670467502850613,122.53229704561002 6.465222348916754,122.73754219954387 6.259977194982881,123.14803250741161 6.259977194982881,123.35327766134549 6.259977194982881,123.96901312314708 5.849486887115148,124.3795034310148 5.644241733181289,124.78999373888254 5.438996579247416,125.40572920068414 5.233751425313557,125.81621950855185 5.028506271379683,125.81621950855185 5.028506271379683,126.2267098164196 4.823261117445824,126.2267098164196 4.823261117445824,126.63720012428732 4.823261117445824,126.63720012428732 4.823261117445824,127.04769043215506 4.823261117445824,127.04769043215506 4.823261117445824,127.45818074002278 4.823261117445824,127.66342589395666 4.823261117445824,127.66342589395666 4.823261117445824,128.07391620182437 4.823261117445824,128.48440650969212 4.823261117445824,128.68965166362597 5.028506271379683,129.1001419714937 5.233751425313557,129.1001419714937 5.233751425313557,129.51063227936143 5.233751425313557,129.51063227936143 5.233751425313557,129.92112258722918 5.233751425313557,130.12636774116302 5.438996579247416,130.53685804903077 5.438996579247416,130.53685804903077 5.644241733181289,130.74210320296464 5.849486887115148,131.15259351083236 6.0547320410490215,131.35783866476623 6.259977194982881,131.35783866476623 6.259977194982881,131.76832897263395 6.670467502850613,131.97357412656783 7.0809578107183455,132.1788192805017 7.286202964652205,132.38406443443554 7.491448118586078,132.58930958836942 7.696693272519937,132.58930958836942 7.696693272519937,132.7945547423033 8.312428734321543,132.99979989623716 8.517673888255402,133.205045050171 8.722919042189261,133.41029020410488 9.133409350056994,133.41029020410488 9.338654503990867,133.41029020410488 9.338654503990867,133.41029020410488 9.954389965792458,133.41029020410488 9.954389965792458,133.41029020410488 10.36488027366019,133.41029020410488 10.775370581527923,133.41029020410488 10.775370581527923,133.41029020410488 11.185860889395656,133.41029020410488 11.596351197263388,133.41029020410488 11.596351197263388,133.41029020410488 12.00684150513112,133.41029020410488 12.00684150513112,133.41029020410488 12.417331812998839,133.41029020410488 12.827822120866571,133.41029020410488 13.033067274800445,133.205045050171 13.238312428734304,132.99979989623716 13.648802736602036,132.7945547423033 14.059293044469769,132.58930958836942 14.469783352337501,132.58930958836942 14.469783352337501,132.38406443443554 14.67502850627136,132.38406443443554 14.67502850627136,132.1788192805017 15.085518814139093,131.97357412656783 15.085518814139093,131.76832897263395 15.290763968072966,131.35783866476623 15.701254275940698,131.15259351083236 16.111744583808417,131.15259351083236 16.111744583808417,130.74210320296464 16.52223489167615,130.3316128950969 16.93272519954388,130.12636774116302 17.137970353477755,130.12636774116302 17.137970353477755,129.92112258722918 17.548460661345473,129.7158774332953 17.548460661345473,129.51063227936143 17.753705815279346,129.51063227936143 17.753705815279346,128.89489681755984 18.16419612314708,128.89489681755984 18.16419612314708,128.48440650969212 18.16419612314708,128.07391620182437 18.16419612314708,127.86867104789053 18.369441277080938,127.45818074002278 18.57468643101481,127.45818074002278 18.57468643101481,126.84244527822119 18.77993158494867,126.84244527822119 18.77993158494867,126.43195497035344 18.77993158494867,126.84244527822119 18.77993158494867))";

              var format = new WKT();

              var feature = format.readFeature(wkt, {
                dataProjection: "EPSG:4326",
                featureProjection: "EPSG:4326",
              });
              feature.data = res.data[i];
              feature.type = "area";
              arrFeature.push(feature);
            } catch (e) {
              console.log("draw area error ", res.data);
            }
          }
          var vector = new VectorLayer({
            source: new VectorSource({
              features: arrFeature,
            }),
          });
          map.addLayer(vector);
        }
        console.log("data vung: ", res);
      } catch (error) {
        console.log(error);
        this.$notify({
          title: "Lỗi",
          message: "Get list area thất bại.",
          position: "top right",
          type: "error",
        });
      }
    },

    handleDeleteFeature() {
      this.removeFeature();
    },
    addInteraction() {
      map.removeInteraction(this.singleClick);
    },

    zoomAndselected(feature) {
      console.log("zoomAndselected ", feature.values_.geometry.getExtent());
      map.getView().fit(feature.values_.geometry.getExtent(), {
        size: [250, 250],
      });
    },

    vungTypeChange(val) {
      this.vungType = val;

      this.clearAllFeature();
      this.removeSelect();
      this.removeTranslate();

      this.handleDrawVung();
    },

    addSelect() {
      map.removeInteraction();
      this.singleClick = new Select();
      map.addInteraction(this.singleClick);

      this.singleClick.getFeatures().on("add", (event) => {
        var properties = event.element.getProperties();
        console.log("select feature,", event);
        this.selectedFeatureID = properties.id;
        this.selectedFeature = properties.dataVung;
        this.$notify({
          title: "Chọn vùng",
          message:
            "Vùng " +
            (event.element.data ? event.element.data.name : properties.name) +
            " đã được chọn.",
          
          type: "info",
        });
        console.log("select feature,", properties);
      });
      this.singleClick.getFeatures().on("remove", (event) => {
        this.selectedFeatureID = null;
        this.selectedFeature = {};
        console.log("deselected", event);
      });
    },
    removeSelect() {
      map.removeInteraction(this.singleClick);
    },

    removeFeature() {
      var features = currentDrawSource.getFeatures();
      if (features != null && features.length > 0) {
        for (var x in features) {
          var properties = features[x].getProperties();
          console.log(properties);
          var id = properties.id;
          if (id == this.selectedFeatureID) {
            console.log("remove: ", properties);
            currentDrawSource.removeFeature(features[x]);
            this.selectedFeatureID = null;
            break;
          }
        }
      }
    },

    /* Feature moving */
    doDrag(feature, pixel) {
      console.log("do drag");
      // for (var f in selectedFeatures) {
      //   if (feature != selectedFeatures[f]) {
      //     var res = map.getResolution();
      //     selectedFeatures[f].geometry.move(
      //       res * (pixel.x - lastPixel.x),
      //       res * (lastPixel.y - pixel.y)
      //     );
      //     vectors.drawFeature(selectedFeatures[f]);
      //   }
      // }
      // lastPixel = pixel;
    },

    /* Featrue stopped moving */
    endDrag(feature, pixel) {
      console.log("end drag");
      // for (f in selectedFeatures) {
      //   f.state = OpenLayers.State.UPDATE;
      // }
    },
  },
};

// First some change in this function.
</script>

<style lang="scss">
@import "~ol/ol.css";
.custom-mouse-position {
  position: absolute;
  bottom: 10px;
  right: 30px;
  float: right;
  width: 170px;
  height: 40px;
}
.el-button-on-map {
  width: 50px !important;

  position: absolute;
  top: 10px;
  right: 6px;
  z-index: 10;
  text-align: right;
}
.el-button-on-map button {
  margin-bottom: 10px;
}
#mapOL {
  width: 100%;
  height: calc(100% - 60px);
  position: fixed;
}
.ol-popup {
  position: absolute;
  background-color: white;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
  padding: 15px;
  border-radius: 10px;
  border: 1px solid #cccccc;
  bottom: 12px;
  left: -400px;
  // top: 200px;
  // left: 200px;
  min-width: 400px;
}
.label-form {
  min-width: 70px;
  width: auto;
  float: left;
  margin-right: 6px;
}
.ol-popup label {
}
.ol-popup:after,
.ol-popup:before {
  top: 100%;
  border: solid transparent;
  content: " ";
  height: 0;
  width: 0;
  position: absolute;
  pointer-events: none;
}
.ol-popup:after {
  border-top-color: white;
  border-width: 10px;
  left: 48px;
  margin-left: -10px;
}
.ol-popup:before {
  border-top-color: #cccccc;
  border-width: 11px;
  left: 48px;
  margin-left: -11px;
}
.ol-popup-closer {
  text-decoration: none;
  position: absolute;
  top: 2px;
  right: 8px;
}
.ol-popup-closer:after {
  content: "✖";
}
.dialog-detail-vessel {
  padding-top: 6px;
}
.hideSidebar .el-icon-map-location {
  margin-left: 20px;
}
.pagination-container {
  overflow: auto;
}
.bt-search-advance {
  float: right;
}
.require {
  color: red;
}
.text-des {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.el-dialog__wrapper {
  position: unset;
}
.el-dialog__footer {
  position: sticky;
  bottom: 0;
}
#dialogListArea {
  margin-top: 10vh;
  // // position: unset !important;
  // // height: 100%;
  // z-index: auto;
  width: 550px;
  // height: max-content;
  left: 5%;
}
#dialogDrawArea {
  margin-top: 10vh;
  // z-index: auto;
  width: 550px;
  // height: max-content;
  left: calc(75% - 250px);
}
#dialogDrawArea .el-dialog {
  margin: 0 !important;
}
#dialogListArea .el-dialog {
  margin: 0 !important;
}
.v-modal {
  position: unset !important;
}
.el-button-on-map button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.el-button-on-map i {
  margin-left: -6px !important;
}
// #dialogListArea .el-dialog__body {
//   height: 450px;
//   overflow-y: auto;
//   overflow-x: hidden;
// }
textarea:focus-visible{
  outline: none;
}
</style>
