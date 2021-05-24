<template>
  <div class="app-container">
    <h2 class="no-margin-top">Danh sách Media đã xử lý</h2>
    <div class="filter-container">
      <template>
        <div class="block">
          <el-button class="search" v-loading="loading" @click="downloadFiles()" type="primary" size="small">Download</el-button>
        </div>
        <div class="block">
          <el-input v-model="query.sourceIps" placeholder="Ip nguồn" size="small"></el-input>
        </div>
        <div class="block">
          <el-input v-model="query.destIps" placeholder="Ip đích" size="small"></el-input>
        </div>
        <div class="block">
          <el-select id="sltMediaType" multiple collapse-tags v-model="query.mediaType" clearable placeholder="Loại media" size="small">
            <el-option v-for="item in mediaTypeLst" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </div>
        <div class="block">
          <el-select id="sltDataSource" multiple collapse-tags v-model="query.dataSource" clearable placeholder="Nguồn thu" size="small">
            <el-option v-for="item in dataSourceLst" :key="item.id" :label="item.sourceName" :value="item.id"></el-option>
          </el-select>
        </div>

        <!-- <el-date-picker v-model="query.startTime" size="small" format="dd/MM/yyyy HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"
         :editable="false" style="width: 160px"
         type="datetime" placeholder="Từ ngày"></el-date-picker>
        <el-date-picker v-model="query.endTime" size="small" format="dd/MM/yyyy HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"
         :editable="false" style="width: 160px; margin-left: 10px;"
         type="datetime" placeholder="Đến ngày"></el-date-picker>
        <el-button class="search" @click="handleFilter()" type="primary" size="small">Tìm kiếm</el-button> -->

        <div class="block">
          <el-date-picker v-model="query.startTime" id="startTimeInput" size="small" format="dd/MM/yyyy HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false" type="datetime" style="width: 160px;" placeholder="Từ ngày"></el-date-picker>
        </div>
        <div class="block">
          <el-date-picker v-model="query.endTime" id="endTimeInput" size="small" format="dd/MM/yyyy HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"
          :editable="false" type="datetime" style="width: 150px; margin-left: 15px;" placeholder="Đến ngày"></el-date-picker>
        </div>
        <div class="block">
          <el-button class="search" @click="handleFilter()" id="btnSearch" type="primary" size="small">Tìm kiếm</el-button>
        </div>

      </template>
    </div>

    <el-table v-loading="loading" :data="list" border fit highlight-current-row style="width: 100%"
     @selection-change="handleSelectionChange()">
      
      <el-table-column align="center" type="selection" width="30">
        <template slot-scope="scope">
          <span><input type="checkbox" v-model="checkedLst" :value="scope.row" /></span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="STT" width="65">
        <template slot-scope="scope">
          <span>{{
            (query.currentPage - 1) * query.rowsPerPage + scope.$index + 1
          }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Nguồn thu" width="190">
        <template slot-scope="scope">
          <span>{{ scope.row.dataSourceNameFrom }} <br /> {{ scope.row.dataSourceNameTo }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Loại" width="105">
        <template slot-scope="scope">
          <span>{{ scope.row.mediaTypeNameFrom }} <br /> {{ scope.row.mediaTypeNameTo }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Ip nguồn" width="120" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.sourceIpFrom }} <br /> {{ scope.row.sourceIpTo }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Ip đích" width="120" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.destIpFrom }} <br /> {{ scope.row.destIpTo }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Chiều dữ liệu" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.directionFrom == 1">Chiều đi</span>
          <span v-else-if="scope.row.directionFrom == 2">Chiều về</span>
          <span v-else>Không xác định</span>
          <br />
          <span v-if="scope.row.directionTo == 1">Chiều đi</span>
          <span v-else-if="scope.row.directionTo == 2">Chiều về</span>
          <span v-else>Không xác định</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="Thời gian" width="140">
        <template slot-scope="scope">
          <span>{{ scope.row.eventTimeFrom | formatDate }} <br /> {{ scope.row.eventTimeTo | formatDate }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Dung lượng" width="125">
        <template slot-scope="scope">
          <span>{{ scope.row.fileSizeFrom }} <br /> {{ scope.row.fileSizeTo }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Trạng thái" width="100" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.statusFrom === 0"><el-tag effect="light">Chưa xem</el-tag></span>
          <span v-else><el-tag type="success" effect="light" >Đã xem</el-tag></span>
          <span v-if="scope.row.statusTo === 0"><el-tag effect="light">Chưa xem</el-tag></span>
          <span v-else><el-tag type="success" effect="light" >Đã xem</el-tag></span>
        </template>
      </el-table-column>

      <el-table-column label="Tác vụ" align="center">
        <template slot-scope="scope">

          <el-button type="primary" size="small" @click="viewDetailMedia(scope.row)">Chi tiết</el-button>

          <!-- form xem chi tiết -->
          <el-dialog top="10px" title="Thông tin 2" :visible.sync="dialogMediaDetailVisible" width="80%"
           :close-on-click-modal="false" @close="closeDialog()">

            <span v-if="mediaDetailSrc && mediaDetailSrc.mediaTypeId === 1 && mediaDetailDest && mediaDetailDest.mediaTypeId === 1"
             slot="title" style="cursor: pointer;" v-loading="loading" @click="downloadMergeAudio();">
              <!-- <i class="el-icon-info"></i> Thông tin -  -->
              <i class="el-icon-download"></i> Tải file ghép
            </span>
            <span v-else slot="title" style="cursor: pointer;"><i class="el-icon-info"></i> Thông tin</span>

              <el-row :gutter="5">
                <el-col :span="12">

                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <!-- Nếu media là Web/html thì gen html ra trước, còn các loại media khác thì gen html = JS -->
                    <div v-if="mediaDetailSrc && mediaDetailSrc.mediaTypeId === 3">
                      <div class="row" style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878; margin-left: 0; margin-right: 0; height: 255px">
                        <div style="height: 15px"></div>
                        <div class="form-row" style="width: 100%">
                          <div class="col-sm-9">
                            <span style="color: red; font-weight: bold">&emsp;{{mediaDetailSrc.filePath}}</span>
                          </div>
                          <div v-if="mediaDetailSrc.filePath.endsWith('.html')" class="col-sm-1 text-right">
                            <a href="javascript:void(0)" @click="viewHtmlWeb('FROM')" style="color: orange; font-weight: bold">Xem</a>
                          </div>
                          <div class="col-sm-2 text-left">
                            <a v-bind:href="mediaDetailSrc.filePath" style="color: blue; font-weight: bold" target="_blank" v-bind:download="mediaDetailSrc.filePath">Tải xuống</a>
                          </div>
                        </div>
                      </div>
                    </div>
                    <!-- Hiển thị media: audio, video, .... -->
                    <div ref="mediaModelVideoAudioFrom"></div>
                  </div>

                </el-col>
                <el-col :span="12">
                  
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <!-- Nếu media là Web/html thì gen html ra trước, còn các loại media khác thì gen html = JS -->
                    <div v-if="mediaDetailDest && mediaDetailDest.mediaTypeId === 3">
                      <div class="row" style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878; margin-left: 0; margin-right: 0; height: 255px">
                        <div style="height: 15px"></div>
                        <div class="form-row" style="width: 100%">
                          <div class="col-sm-9">
                            <span style="color: red; font-weight: bold">&emsp;{{mediaDetailDest.filePath}}</span>
                          </div>
                          <div v-if="mediaDetailDest.filePath.endsWith('.html')" class="col-sm-1 text-right">
                            <a href="javascript:void(0)" @click="viewHtmlWeb('TO')" style="color: orange; font-weight: bold">Xem</a>
                          </div>
                          <div class="col-sm-2 text-left">
                            <a v-bind:href="mediaDetailDest.filePath" style="color: blue; font-weight: bold" target="_blank" v-bind:download="mediaDetailDest.filePath">Tải xuống</a>
                          </div>
                        </div>
                      </div>
                    </div>
                    <!-- Hiển thị media: audio, video, .... -->
                    <div ref="mediaModelVideoAudioTo"></div>
                  </div>

                </el-col>
              </el-row>

              <el-row :gutter="5">

                <el-col :span="6">
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row>
                      <el-col :span="6" class="aliasLeft">ID nguồn</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailSrc.srcId }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Loại</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailSrc.srcIsHq === 1 ? 'Bờ' : 'Trên biển' }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Tên</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailSrc.srcName }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">SĐT</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailSrc.sourcePhone }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">IP</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailSrc.sourceIp }}</el-col>
                    </el-row>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row>
                      <el-col :span="6" class="aliasLeft">ID đích</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailSrc.destId }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Loại</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailSrc.destIsHq === 1 ? 'Bờ' : 'Trên biển' }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Tên</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailSrc.destName }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">SĐT</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailSrc.destPhone }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">IP</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailSrc.destIp }}</el-col>
                    </el-row>
                  </div>
                </el-col>

                <el-col :span="6">
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row>
                      <el-col :span="6" class="aliasLeft">ID nguồn</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailDest.srcId }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Loại</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailDest.srcIsHq === 1 ? 'Bờ' : 'Trên biển' }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Tên</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailDest.srcName }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">SĐT</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailDest.sourcePhone }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">IP</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailDest.sourceIp }}</el-col>
                    </el-row>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row>
                      <el-col :span="6" class="aliasLeft">ID đích</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailDest.destId }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Loại</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailDest.destIsHq === 1 ? 'Bờ' : 'Trên biển' }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">Tên</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailDest.destName }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">SĐT</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailDest.destPhone }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="6" class="aliasLeft">IP</el-col>
                      <el-col :span="18" class="textRight">{{ mediaDetailDest.destIp }}</el-col>
                    </el-row>
                  </div>
                </el-col>

                <el-col :span="12">
                  <!-- <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Loại media</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailSrc.mediaTypeName }}</el-col>
                      <el-col :span="4" class="aliasLeft">Trạng thái</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailSrc.status === 1 ? 'Đã xem' : 'Chưa xem' }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Thời gian</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailSrc.eventTime | formatDate }}</el-col>
                      <el-col :span="4" class="aliasLeft">Nguồn thu</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailSrc.dataSourceName }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Dung lượng</el-col>
                      <el-col :span="20" class="textRight">{{ mediaDetailSrc.fileSize }}</el-col>
                    </el-row>
                  </div> -->
                </el-col>
              </el-row>

              <el-row :gutter="5">
                <el-col :span="12">
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Loại media</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailSrc.mediaTypeName }}</el-col>
                      <el-col :span="4" class="aliasLeft">Trạng thái</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailSrc.status === 1 ? 'Đã xem' : 'Chưa xem' }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Thời gian</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailSrc.eventTime | formatDate }}</el-col>
                      <el-col :span="4" class="aliasLeft">Nguồn thu</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailSrc.dataSourceName }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Dung lượng</el-col>
                      <el-col :span="20" class="textRight">{{ mediaDetailSrc.fileSize }}</el-col>
                    </el-row>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Loại media</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailDest.mediaTypeName }}</el-col>
                      <el-col :span="4" class="aliasLeft">Trạng thái</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailDest.status === 1 ? 'Đã xem' : 'Chưa xem' }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Thời gian</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailDest.eventTime | formatDate }}</el-col>
                      <el-col :span="4" class="aliasLeft">Nguồn thu</el-col>
                      <el-col :span="8" class="textRight">{{ mediaDetailDest.dataSourceName }}</el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="4" class="aliasLeft">Dung lượng</el-col>
                      <el-col :span="20" class="textRight">{{ mediaDetailDest.fileSize }}</el-col>
                    </el-row>
                  </div>
                </el-col>
              </el-row>

              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogMediaDetailVisible = false">Đóng</el-button>
              </div> 
          </el-dialog>

          <el-dialog top="10px" title="Html page" :visible.sync="dialogMediaDetailHtmlWebVisibleFrom" width="90%" height="100%" :close-on-click-modal="false">
              <el-row :gutter="5">
                <el-col :span="24">
                  <div v-if="mediaDetailSrc && mediaDetailSrc.mediaTypeId === 3 && mediaDetailSrc.filePath.endsWith('.html')" class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row style="border: 1px solid #FFC9CD;">
                      <el-col :span="24">
                        <iframe id="mediaListModelCommonViewWebHtmlIframeFrom" :src="mediaDetailSrc.filePath" width="100%" height="100%" frameBorder="0"></iframe>
                      </el-col>
                    </el-row>
                  </div>
                </el-col>
              </el-row>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogMediaDetailHtmlWebVisibleFrom = false">Đóng</el-button>
              </div> 
          </el-dialog>

          <el-dialog top="10px" title="Html page" :visible.sync="dialogMediaDetailHtmlWebVisibleTo" width="90%" height="100%" :close-on-click-modal="false">
              <el-row :gutter="5">
                <el-col :span="24">
                  <div v-if="mediaDetailDest && mediaDetailDest.mediaTypeId === 3 && mediaDetailDest.filePath.endsWith('.html')" class="grid-content bg-purple" style="border: 1px solid #FFC9CD;">
                    <el-row style="border: 1px solid #FFC9CD;">
                      <el-col :span="24">
                        <iframe id="mediaListModelCommonViewWebHtmlIframeTo" :src="mediaDetailDest.filePath" width="100%" height="100%" frameBorder="0"></iframe>
                      </el-col>
                    </el-row>
                  </div>
                </el-col>
              </el-row>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogMediaDetailHtmlWebVisibleTo = false">Đóng</el-button>
              </div> 
          </el-dialog>

        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 10" :total="total" :page.sync="query.currentPage" :limit.sync="query.rowsPerPage" @pagination="getList" />

  </div>
</template>

<script>
import Pagination from "@/components/Pagination";
import Resource from "@/api/resource";
import axios from 'axios';

const dataSourceResource = new Resource("manager/source/get-all");
const mediaTypeResource = new Resource("manager/media-type/get-all");
const mediaResource = new Resource("media/list-relation/search");
const updateMediaReadStatusResource = new Resource("media/list/updateStatusMediaId");
const detailMediaResource = new Resource("media/detail-relation");

let yesterdayTime = new Date();
yesterdayTime.setDate(yesterdayTime.getDate() - 7);
let dayYesterday = checkZero(yesterdayTime.getDate() + '');
let monthYesterday = checkZero((yesterdayTime.getMonth() + 1) + '');

export default {
  components: {
    Pagination,
  },
  directives: {},
  data() {
    return {
      dialogMediaDetailVisible: false,
      dialogMediaDetailHtmlWebVisibleFrom: false,
      dialogMediaDetailHtmlWebVisibleTo: false,
      list: null,
      total: 0,
      loading: true,
      query: {
        startTime: yesterdayTime.getFullYear() + '-' +  monthYesterday + '-' +  dayYesterday + ' 00:00:00',
        endTime: getCurrentDateToStr(new Date()),
        currentPage: 1,
        rowsPerPage: 20,
        sourceIps: "",
        destIps: "",
        mediaType: [],
        dataSource: []
      },
      dataSourceLst: [],
      mediaTypeLst: [],
      mediaDetailSrc: {},
      mediaDetailDest: {},
      checkedLst: [],
      allSelected: false
    };
  },
  created() {
    this.getAllDataSource();
    this.getAllMediaType();
    //this.getList();
  },
  methods: {
    async getAllDataSource() {
      const { data } = await dataSourceResource.list();
      if(data) {
        this.dataSourceLst = data;
        this.getList();
      }
    },
    async getAllMediaType() {
      const { data } = await mediaTypeResource.list();
      if(data)
        this.mediaTypeLst = data;
    },
    getList() {
      if( this.query.startTime >= this.query.endTime ) {
        alert('Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!');
        return;
      }

      this.loading = true;
      this.checkedLst = [];
      console.log('==> query: ' + JSON.stringify(this.query));
      mediaResource.listByPost(this.query).then((mediaDataLst) => {
        // console.warn(console.log('<== mediaDataLst: ' + JSON.stringify(mediaDataLst)));
        if( mediaDataLst.data && mediaDataLst.data.length > 0 ) {
          this.list = mediaDataLst.data;
          this.total = mediaDataLst.total;

          let sysHasDataSrc = this.dataSourceLst && this.dataSourceLst.length > 0;
          this.list.forEach(item => {
            item.fileSizeFrom = commonFormatBytesToSize(item.fileSizeFrom);
            item.fileSizeTo = commonFormatBytesToSize(item.fileSizeTo);
            if( sysHasDataSrc ) {
              let arr = this.findValuesInArray(item.dataSourceFrom, item.dataSourceTo, this.dataSourceLst);
              if( arr.length > 0 ) {
                item.dataSourceNameFrom = arr[0];
                item.dataSourceNameTo = arr[1];
              }
            }
          });
        }else {
          console.info('mediaDataLst is empty!');
          this.list = [];
          this.total = 0;
        }
        this.loading = false;
      }).catch((err) => {
        console.error(err)
      });
    },
    findValuesInArray(dataSourceFrom, dataSourceTo, arrCheck) {
      let arrRes = [];
      let matchCount = 0;
      for( let i=0; i<arrCheck.length; i++ ) {
        if( matchCount > 1 )
          break;
        if( dataSourceFrom == arrCheck[i].id ) {
          arrRes.push(arrCheck[i].sourceName);
          matchCount++;
        }else if( dataSourceTo == arrCheck[i].id ) {
          arrRes.push(arrCheck[i].sourceName);
          matchCount++;
        }
      }
      return arrRes;
    },
    mediaListBuildVideoAudioByMediaId(filePath, mediaTypeId, elementId) {
      let html = '';
      if (mediaTypeId === 1) // audio
        html = '<video id="' + elementId + '" width="100%" height="255px" controls style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878;">' +
                '    <source src="' + filePath + '" type="audio/wav">' +
                '</video>';
      else if ( mediaTypeId === 2 ) { // video
          if ( filePath.endsWith('.ts') ) // video stream
              html = '<video id="' + elementId + '" width="100%" height="255px" controls style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878;">' +
                      '</video>';
          else // mp4, etc,....
              html = '<video id="' + elementId + '" width="100%" height="255px" controls style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878;">' +
                      '    <source src="' + filePath + '" type="video/mp4">' +
                      '</video>';
      } else if (mediaTypeId === 3) { // Web
          // Không gen ra html vì đã gen html sẵn rồi ( <div v-if="mediaDetail.mediaTypeId === 3"> ), case này code ra nhìn cho rõ ràng chứ ko có case này xảy ra được, do đã check mediaTypeId <> 3 từ trước khi vào hàm này.
      } else { // Email || transfer file
          html = '<div class="row" style="background-color: #f6f6f6; border-radius: 2px; border: 1px solid #7f7878; margin-left: 0; margin-right: 0; height: 255px">' +
                  '     <div style="height: 15px"></div>' +
                  '     <div class="form-row" style="width: 100%">' +
                  '         <div class="col-sm-9">' +
                  '             <span style="color: red; font-weight: bold">&emsp;' + filePath + '</span>' +
                  '         </div>';
          if ( mediaTypeId !== 8 && filePath.endsWith('.html') ) {
              html += '         <div class="col-sm-1 text-right">' +
                      '             <a href="' + filePath + '" style="color: orange; font-weight: bold" target="_blank">Xem</a>' +
                      '         </div>';
          }
          html += '				<div class="col-sm-2 text-left">' +
                  '             <a href="' + filePath + '" style="color: blue; font-weight: bold" target="_blank">Tải xuống</a>' +
                  '         </div>' +
                  '     </div> ' +
                  ' </div>';
      }
      return html;
    },
    viewHtmlWeb(type) {
      if( type === 'FROM' ) {
        if( this.dialogMediaDetailHtmlWebVisibleTo )
          this.dialogMediaDetailHtmlWebVisibleTo = false;
        this.dialogMediaDetailHtmlWebVisibleFrom = true;
      }else if( type === 'TO' ) {
        if( this.dialogMediaDetailHtmlWebVisibleFrom )
          this.dialogMediaDetailHtmlWebVisibleFrom = false;
        this.dialogMediaDetailHtmlWebVisibleTo = true;
      }
    },
    viewDetailMedia(media) {
      this.dialogMediaDetailVisible = true;

      this.mediaDetailSrc = {};
      this.mediaDetailDest = {};

      // Lấy chi tiết 2 bản ghi media
      let payLoad = {
        uuidKeyFrom: media.uuidKeyFrom,
        uuidKeyTo: media.uuidKeyTo,
        partName: media.partNameFrom
      };
      console.log('==> payLoad: ' + JSON.stringify(payLoad))
      detailMediaResource.listByPost(payLoad).then((mediaRelation) => {
        console.log('<== mediaRelation: ' + JSON.stringify(mediaRelation));
        if( mediaRelation.data && mediaRelation.data.length === 2 ) {

          this.mediaDetailSrc = mediaRelation.data[0];
          this.mediaDetailDest = mediaRelation.data[1];
          
          if( this.mediaDetailSrc && this.mediaDetailSrc.filePath ) {
            if( this.mediaDetailSrc.mediaTypeId === 3 ) {

              // Xóa phần hiển thị cũ như Audio, video, .... nếu trước đó đã từng gen ra content.
              const mediaElement = this.$refs.mediaModelVideoAudioFrom;
              if( mediaElement )
                mediaElement.innerHTML = '';

            }else { // Xử lý gen html để hiển thị Audio, video, ....
              let htmlVideoAudio = this.mediaListBuildVideoAudioByMediaId(this.mediaDetailSrc.filePath
                                                  , this.mediaDetailSrc.mediaTypeId, 'mediaPlayerIdFrom');
              this.$nextTick(() => {

                const mediaElement = this.$refs.mediaModelVideoAudioFrom;
                if( mediaElement )
                  mediaElement.innerHTML = htmlVideoAudio;

                if( this.mediaDetailSrc.filePath.endsWith('.ts') ) {
                  let m3u8Resource = this.mediaDetailSrc.filePath.substring(0, this.mediaDetailSrc.filePath.lastIndexOf('.ts')) + '.m3u8';
                  let videoData = document.getElementById('mediaPlayerIdFrom');
                  if (Hls.isSupported()) {
                    let hls = new Hls();
                    hls.loadSource(m3u8Resource);
                    hls.attachMedia(videoData);
                    hls.on(Hls.Events.MANIFEST_PARSED, function () {
                        videoData[0].play();
                    });
                  }
                }
              });
            }
          }

          if( this.mediaDetailDest && this.mediaDetailDest.filePath ) {
            if( this.mediaDetailDest.mediaTypeId === 3 ) {

              // Xóa phần hiển thị cũ như Audio, video, .... nếu trước đó đã từng gen ra content.
              const mediaElement = this.$refs.mediaModelVideoAudioTo;
              if( mediaElement )
                mediaElement.innerHTML = '';

            }else { // Xử lý gen html để hiển thị Audio, video, ....
              let htmlVideoAudio = this.mediaListBuildVideoAudioByMediaId(this.mediaDetailDest.filePath
                                                  , this.mediaDetailDest.mediaTypeId, 'mediaPlayerIdTo');
              this.$nextTick(() => {

                const mediaElement = this.$refs.mediaModelVideoAudioTo;
                if( mediaElement )
                  mediaElement.innerHTML = htmlVideoAudio;

                if( this.mediaDetailDest.filePath.endsWith('.ts') ) {
                  let m3u8Resource = this.mediaDetailDest.filePath.substring(0, this.mediaDetailDest.filePath.lastIndexOf('.ts')) + '.m3u8';
                  let videoData = document.getElementById('mediaPlayerIdTo');
                  if (Hls.isSupported()) {
                    let hls = new Hls();
                    hls.loadSource(m3u8Resource);
                    hls.attachMedia(videoData);
                    hls.on(Hls.Events.MANIFEST_PARSED, function () {
                        videoData[0].play();
                    });
                  }
                }
              });
            }
          }

          // Đánh dấu đã xem
          let payLoadUpdatReadStatus = {
            uuidKeys: [media.uuidKeyFrom, media.uuidKeyTo],
            updateType: 'MEDIA_RELATION'
          };
          updateMediaReadStatusResource.listByPost(payLoadUpdatReadStatus).then((res) => {
            console.log('<== updateMediaReadStatusResource.res: [' + JSON.stringify(res) + ']');
            if( res && res.data )
              media.statusFrom = 1;
              media.statusTo = 1;
          }).catch((err) => {
            console.error(err);
          });

        }else {
          console.error('mediaRelation is invalid');
          console.error(mediaRelation);
          setTimeout(() => {
            this.dialogMediaDetailVisible = false;
          }, 500);
          //alert('Bản ghi lỗi!');
        }
      }).catch((err) => {
        console.error(err)
      });

    },
    handleFilter() {
      this.query.currentPage = 1;
      this.getList();
    },
    downloadFiles() {
      this.loading = !this.loading;
      // console.log(JSON.stringify(this.checkedLst));
      if( this.checkedLst.length > 0 ) {
        axios.post(process.env.VUE_APP_FILE_API + 'download-media-files-relation'
                , { mediaFiles: this.checkedLst }
                , { responseType: 'blob' })
        .then(res => {
          if( res.data ) {
            const blob = new Blob([res.data], { type: res.data.type });
            const link = document.createElement('a');
            link.href = URL.createObjectURL(blob);
            // console.warn(res);
            link.setAttribute('download', 'vsat-media-files-processed.zip');
            link.click();
            URL.revokeObjectURL(link.href);
          }
          this.loading = !this.loading;
        })
        .catch(e => {
          console.error(e);
          this.loading = !this.loading;
        });
      }else {
        alert('Chưa chọn file cần tải!');
        this.loading = !this.loading;
      }
    },
    handleSelectionChange() {
      if( this.list.length === 0 )
        return;
      if( this.checkedLst.length > 0 )
        this.checkedLst = [];
      if ( !this.allSelected ) {
        this.checkedLst = Array.from(this.list);
        this.allSelected = true;
      }else
        this.allSelected = false;
    },
    downloadMergeAudio() {
      this.loading = !this.loading;
      let payLoad = {
        filePathFrom: this.mediaDetailSrc.filePath,
        filePathTo: this.mediaDetailDest.filePath
      };
      axios.post(process.env.VUE_APP_FILE_API + 'merge-and-download-audio', payLoad, { responseType: 'blob' })
      .then(res => {
        if( res.data ) {
          const blob = new Blob([res.data], { type: res.data.type });
          const link = document.createElement('a');
          link.href = URL.createObjectURL(blob);
          // console.warn(res);
          link.setAttribute('download', 'vsat-audio-file.zip');
          link.click();
          URL.revokeObjectURL(link.href);
        }
        this.loading = !this.loading;
      })
      .catch(e => {
        console.error(e);
        this.loading = !this.loading;
      });
    },
    closeDialog () {
      stopPlayMedia('mediaPlayerIdFrom');
      stopPlayMedia('mediaPlayerIdTo');
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
  margin-right: 5px;
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
.el-input__inner {
  width: 150px;
}

 .el-row {
    margin-bottom: 5px;
    &:last-child {
      margin-bottom: 0px;
    }
  }
  .el-col {
    border-radius: 4px;
  }
 
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .bg-purple {
    background: #F2F2F2;
  }
  .aliasLeft {
    text-align: left;
    padding-left: 5px;
    font-size: 11px;
    font-weight: bold;
  }
  .textRight {
    text-align: left;
    padding-left: 5px;
  }
  // .el-select>.el-input input {
  //     // width: 225px;
  //     width: 175px;
  // }
  // .el-input input {
  //     width: 160px;
  // }
  #sltMediaType {
    width: 185px;
  }
  #sltDataSource {
    width: 175px;
  }
  #startTimeInput {
    width: 185px;
  }
  #endTimeInput {
    width: 185px;
    // margin-left: 5px;
  }
  #btnSearch {
    padding-left: 30px;
  }
</style>
