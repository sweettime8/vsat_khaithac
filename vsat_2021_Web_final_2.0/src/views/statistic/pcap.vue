<template>
  <div class="app-container">
    <h2 class="no-margin-top">Thống kê theo loại giao thức</h2>
    <div class="filter-container">
        <el-row :gutter="20" >
        <div >
            <template>            
              <div class="block">
                  <el-select  v-model="optionDate" filterable  @change="changeOption()" style="width :120px;vertical-align: center;">
                    <el-option v-for="item in list_option" :key="item.id" :label="item.value" :value="item.id" >                          
                    </el-option>
                   </el-select>
              </div>
              <div class="block" v-show="optionDate == 0" >
                <div class="block">
                <el-date-picker v-model="query.startDate"  size="small" format="dd-MM-yyyy HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"
                :editable="false" type="datetime" style="width :200px;vertical-align: center;" ></el-date-picker>                
                
                </div>    
                <div class="block">
                <el-date-picker v-model="query.endDate"  size="small" format="dd-MM-yyyy HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"
                :editable="false" type="datetime" style="width :200px;vertical-align: center;" ></el-date-picker>         
                </div>    
              </div>    

              <div class="block" v-show="optionDate == 1">
                <div class="block" >
                  <span style="margin-left:20px">Từ tuần : </span>
                  <el-select v-model="startWeek"  size="small" @change="changeStartWeekOption()"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_week" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>                               
                </div> 
                <span style="margin-right:3px" >/ </span>    
                <div class="block">
                  <el-select v-model="startWeekYear"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_year" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>        
                </div> 

                <div class="block" >
                  <span style="margin-left:20px">Đến tuần : </span>
                  <el-select v-model="endWeek"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_week" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>                               
                </div> 
                <span style="margin-right:3px" >/ </span>    
                <div class="block">
                  <el-select v-model="endWeekYear"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_year" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>        
                </div>

              </div>    

              <div class="block" v-show="optionDate == 2">
                <div class="block" >
                  <span style="margin-left:20px">Từ tháng : </span>
                  <el-select v-model="startMonth"  size="small" @change="changeStartWeekOption()"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_month" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>                               
                </div> 
                <span style="margin-right:3px" >/ </span>    
                <div class="block">
                  <el-select v-model="startMonthYear"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_year" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>        
                </div> 

                <div class="block" >
                  <span style="margin-left:20px">Đến tháng : </span>
                  <el-select v-model="endMonth"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_month" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>                               
                </div> 
                <span style="margin-right:3px" >/ </span>    
                <div class="block">
                  <el-select v-model="endMonthYear"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_year" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>        
                </div> 
              </div> 

              <!-- ádasd -->

              <div class="block">
                  <el-select  v-model="query.dataSource" filterable  @change="changeOptionDataSource()" placeholder="Nguồn dữ liệu" clearable style="vertical-align: center;">
                    <el-option v-for="item in dataSourceLst" :key="item.id" :label="item.sourceName" :value="item.id">            
                    </el-option>
                   </el-select>
              </div>                                    
              <el-button class="search" @click="handleFilter()" type="primary">Tìm kiếm</el-button> 
          </template>
        </div>
      </el-row>
    </div>
    <el-row>
      <el-col :span="8">
       <label for="jstree_protocol" class="title-tree">Cây giao thức</label> 
       <!-- <v-jstree :data="dataTree" show-checkbox multiple allow-batch whole-row @item-click="itemClick"></v-jstree>  -->
       <fieldset>     
       	  <div id="jstree_protocol" class=""></div>
       </fieldset>     

      </el-col>
      <el-col :span="16">
        <el-row>
            <div class="canvas-node" id="mediaChartJsPieDoughnutProtocolTotalNowInitDiv">
                <div class="">
	                    	<div id="event_result"></div>
	              </div>
                <div class="card-content collapse show">
	                      <div>
	                         <template>                      
                              <DoughnutChart
                                :data="pieChartData"
                                :options="pieOptions"
                                v-if="renderComponent"
                              />
                          </template>
	                      </div>
	                    </div>
                	</div>
        </el-row>
      </el-col>           
    </el-row>
    <el-row>
        <template>
            
        <LineChart
                :data="lineChartData"
                :options="lineOptions"
                v-if="renderComponent"
              />
           
      </template>
    </el-row>


  </div>
</template>



<script>
import Pagination from "@/components/Pagination";
import waves from "@/directive/waves"; // Waves directive
import Resource from "@/api/resource";
import { mapGetters } from 'vuex';
import { validString } from "@/utils";

import PieChart from "./components/PieChart.vue";
import DoughnutChart from "./components/DoughnutChart.vue";
import LineChart from "./components/LineChart.vue";
import jstree from 'jstree'

// import $ from "jquery";

const dataSourceResource = new Resource("manager/source/get-all");
const pcapResource = new Resource("statistic/pcap");

let today = new Date();
let day = checkZero(today.getDate() + '');
let month = checkZero((today.getMonth() + 1) + '');
let year = checkZero((today.getFullYear() + 1) + '');

export default {
  components: {
    Pagination,
    DoughnutChart,
    LineChart,
  },
  directives: { waves },
  data() {
    return {
      renderComponent: true,
      list: null,
      dataSourceLst: null,
      total: 0,
      loading: true,
      loadingPie:true,
      optionDate:0,
      query: {
          currentPage: 1,
          rowsPerPage: 10,
          sort: "createdAt", 
          keyword: "",
          optionDate:0,
          startDate: today.getFullYear() + '-' +  month + '-' +  day + ' 00:00:00',
          endDate: getCurrentDateToStr(today),
          dataSource:null,

      },

      startWeek :null,
      endWeek:null,
      startWeekYear:null,
      endWeekYear:null,
      startMonth :null,
      endMonth:null,
      startMonthYear:null,
      endMonthYear:null,

      list_week :null,
      list_month:null,
      list_year :null,

      filterOption:{
          id: 0,
          value: "Theo ngày",
      },
      list_option: [
        {
          id: 0,
          value: "Theo ngày",
        },
        {
          id: 1,
          value: "Theo tuần",
        },
        {
          id: 2,
          value: "Theo tháng",
        },
      ],

      list_pieChartData : null,
      list_ApiPieData:null,    
      pieChartData: {
                labels: [],
                datasets: [{
                  label: '',
                  backgroundColor: [],
                  data: []
                }]
      },
      pieOptions: {
        title: {
            display: false,
                  text: ''
            },
              responsive: true,
                maintainAspectRatio: false
      },
//line
    list_lineChartData:null,
    list_ApiLineData:null, 
    lineChartData: {
        labels: [],
        datasets: [
 
        ]
      },
      lineOptions: {
        title: {
          display: true,
          text: 'Biểu đồ biến động dữ liệu'
        },
        responsive: true,
        maintainAspectRatio: false
      },

    datasets: [
          {
            data: [],
            label: '',
            borderColor: ''
    }],
    totalPacket : null,

    checkParrent : false,
    dataProtocolResult : null,
    dataProtocol : null,
    status_update : false ,
//
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
    this.getList();
    this.getAllDataSource();
    this.setDateValueFirst();
  },

  methods: {
    async getList() {
      this.loading = true;
      this.query.keyword = this.query.keyword.trim();
      // this.query.startDate = "2021-04-12 00:00:00";
      // this.query.endDate =  "2021-04-13 00:00:00";
      const { data, total } = await pcapResource.listByPost(this.query);
      this.list = data;
      
      if (data) {
        this.total = total;

        var totalByte = 0;
        for(var i=0; i< this.list.length; i++){
          totalByte += this.list[i].byteCount
        }
        this.totalPacket = formatCapacity(totalByte);
        //this.dataTree[0].text = 'PACKET[' +this.totalPacket +']' 

        let result_data = [];
        let first_record = {uuid:""};
        this.checkParrent = false;
        var tree_data = this.groupTreeByProtoco(data);
        this.dataProtocolResult = data;
        this.dataProtocol = tree_data;
        //get first element 
        let count = 0;
        
        tree_data.forEach(element => {
        	console.log(element);
        	if(element[1].parentUuid == "null" && count == 0){
        		first_record = element[1];
        		count++;
        	}

        	let row = {
        		id: element[1].uuid,
        		parent: element[1].parentUuid,
        		text : element[1].name + "[" + formatCapacity(element[1].byteCount) + "]",
        		icon : "la la-pie-chart",
        		state : {
        		    disabled  : false
        		 },
        		 a_attr: {
        			 packet_name: element[1].name
        		 }
        	};

        	result_data.push(row);
          
        });


        console.log(result_data)
        if(result_data.length == 0){
        	result_data.push("Không có dữ liệu");
        }

        if(this.checkParrent == false && result_data.length > 0){ 
        	let row = {
        		id: 'PACKET',
        		parent: "#",
        		text : 'PACKET[' +this.totalPacket +']' ,
        		icon : "people",
        		state : {
        		    disabled  : false
        		 },
        		 a_attr: {
        			 packet_name: "PACKET"
        		 }
        	};
          result_data.push(row);
        }


        $('#jstree_protocol').jstree({
          'core' : {
            "multiple" : true,
              "animation" : 0,
              "check_callback" : true,
              'data' : result_data
          },
          "types" : {
              "default" : {
                "icon" : "ft-plus-square"
              },
              "demo" : {
                "icon" : "ft-zap"
              }
          },
          "checkbox" : {
            "keep_selected_style" : false
          },
          "plugins" : [ "types", "state", "checkbox", "changed"]
    	  });

      
      // $('#jstree_protocol').jstree({ 'core' : {
      //     'data' : result_data
      //   }});


        $('#jstree_protocol').on('changed.jstree',  (e, data) => {
            let i, j, r = [], elm_selected = [];
            for(i = 0, j = data.selected.length; i < j; i++) {
              r.push(data.instance.get_node(data.selected[i]).text);
              elm_selected.push(data.selected[i]);
            }
            if(data.selected.length > 0){
              
              $('#event_result').html('Selected: ' + r.join(', '));
            } else {
              $('#event_result').html('Selected: ');
            }
          // create pie chartist
    	    this.mediaActionChoiseElement(elm_selected);  
          
          //create line chartist
    	    let data_group_day = this.dataGroupByDay(elm_selected);
    	    let data_chartist_line = Array.from(data_group_day.data);
    	    this.initChartistFlrlToBuild(data_chartist_line, data_group_day.lables);

        }).jstree();    
        
        // selected not working ( null data)
        if(this.status_update == true){
    		$("#jstree_protocol").jstree(true).settings.core.data = result_data;
        	$("#jstree_protocol").jstree(true).refresh();
    	  } else {
		    	if(typeof first_record.uuid != undefined && first_record.uuid != ""){
            $("#jstree_protocol").on("loaded.jstree", function(){
                $('#jstree_protocol').jstree(true).select_node(first_record.uuid);
            });
          }
        }        
      }


      this.loading = false;
    }, 

     groupTreeByProtoco(data){
        let tree_data = new Map();
        data.forEach(elm => {
          let dataPercent = parseInt(elm.dataPercent, 10);
          let byteCount = elm.byteCount;
          let e = {
            uuid: elm.fullPath,
            parentUuid: "",
            fullPath: elm.fullPath,
            dataPercent: dataPercent,
            byteCount: byteCount,
            name: elm.name
          };
          if( !elm.fullPath.includes('->') && (!elm.parentUuid || elm.parentUuid == "null" ) ) {          
            e.parentUuid = "#";
            this.checkParrent = true;
          } else {
            e.parentUuid = elm.fullPath.substring(0, elm.fullPath.lastIndexOf('->'));
          }
          if (tree_data.has(elm.fullPath)){
            let protocol = tree_data.get(elm.fullPath);
            protocol.dataPercent = protocol.dataPercent + dataPercent;
            protocol.byteCount = protocol.byteCount + byteCount;
          } else {
            tree_data.set(elm.fullPath, e);
          }
        });
        return Array.from(tree_data);
      },

    mediaActionChoiseElement(arr_selected){
        this.mediaChartJsProtpcolTotalTextCenterBuild(arr_selected);
        // chua xu ly truong ho chuyen trang bi may canvas
    },

   mediaChartJsProtpcolTotalTextCenterBuild(arr_selected) {
      let Color = commomColor();
      let labelValues = [];
      let dataValues = [];
      let backgroundColor = [];
      let count = 0;
      let initialValue = 0
        let sumByte = 0;
      
      if(arr_selected.length > 0 && this.dataProtocol.length > 0){
        //filter data
        let record = this.dataProtocol.filter(function(element) {
          return $.inArray(element[0], arr_selected) > -1;
        });
        // get sum value byte
          sumByte = record.reduce(function (accumulator, currentValue) {
              return accumulator + currentValue[1].byteCount
          }, initialValue);
        record.forEach(elm => {
          labelValues.push(elm[1].name + " (" + parseFloat((elm[1].byteCount/sumByte)*100).toFixed(2) + "%)");
          dataValues.push((elm[1].byteCount/sumByte)*100);
            backgroundColor.push(Color[count]);
            count++;
          });
      }

          this.pieChartData.labels = labelValues;
          this.pieChartData.datasets[0].data = dataValues;
          this.pieChartData.datasets[0].backgroundColor = backgroundColor;
          this.pieOptions.title.display = true;
          this.pieOptions.title.text = '';
          this.forceRerender()

    },

    dataGroupByDay(elm_selected){
      let object_data = new Map();
      let arr_lables = [];
      if(elm_selected.length == 0 || this.dataProtocolResult.length == 0){
        return {data: object_data, lables: arr_lables};
      }
      let data = this.dataProtocolResult.filter(function(element) {
        return $.inArray(element.fullPath, elm_selected) > -1;
      });
      
      for(var k = 0; k < data.length; k++) {
        let date_time = new Date(data[k].endTime);
        // check date type search
        let keyByDate = date_time.getFullYear() + "/" + commonMonthKeyToText(date_time.getMonth()) + "/" + date_time.getDate();
        if(!arr_lables.includes(keyByDate)){
          arr_lables.push(keyByDate);
        }
        
        let arr_data = new Array();
        let element_day = null;
        let dataPercent = parseInt(data[k].dataPercent, 10);
        let byteCount = parseInt(data[k].byteCount, 10);
        
        if(object_data.has(data[k].name)){
          arr_data = object_data.get(data[k].name);
          // filter day exist
          let get_day_of_week = arr_data.filter((elm, index, arr) => {
              if(elm.id == keyByDate){
                arr[index].dataPercent += dataPercent;
                arr[index].byteCount += byteCount;
              }
              return elm.id == keyByDate;
            });
          if(get_day_of_week.length == 0) {
            element_day = {
              id: keyByDate,
              uuid: data[k].fullPath,
              parentUuid: "",
              fullPath: data[k].fullPath,
              dataPercent: dataPercent,
              byteCount: byteCount,
              name: data[k].name
            };
            arr_data.push(element_day);
          }
        } else {
          element_day = {
            id: keyByDate,
            uuid: data[k].fullPath,
            parentUuid: "",
            fullPath: data[k].fullPath,
            dataPercent: dataPercent,
            byteCount: byteCount,
            name: data[k].name
          };
          arr_data.push(element_day);
        }
        object_data.set(data[k].name, arr_data);
      }
      if(arr_lables.length > 1){
        arr_lables.sort(function(a, b){ return parseInt(commonRemoveSlash(a),10) - parseInt(commonRemoveSlash(b),10) });
      }
      return {data: object_data, lables: arr_lables};
    },

    async getAllDataSource() {
      const { data } = await dataSourceResource.list();
      if(data){
        this.dataSourceLst = data;
      }
    },

    async setDateValueFirst(){
      var week = [];
      var setYear = [];
      var setMonth = [];
      for(var i = 0 ; i<=52;i++){
        week[i] = i + 1;
      } 
      for(var i = 0 ; i <= 11 ; i++){
        setMonth[i] = i + 1;
      }
      for(var i=0;i<=20;i++){
        setYear[i] = year - i-1;
      }
      this.list_week = week;
      this.list_year = setYear;
      this.list_month = setMonth;
    },

    changeOption(){
      console.log("changeOption");
      if(this.optionDate == 0){
          this.query.startDate = today.getFullYear() + '-' +  month + '-' +  day + ' 00:00:00',
          this.query.endDate = getCurrentDateToStr(today);
      }
      if(this.optionDate == 1){
        this.getWeekNumber();
      }
      if(this.optionDate == 2){
        this.startMonth = today.getMonth() +1;
        this.endMonth = today.getMonth() +1;
        this.startMonthYear = today.getFullYear();
        this.endMonthYear = today.getFullYear();
      }

      
    },

    changeOptionDataSource(){
          console.log("changeOptionDataSource");
    },    

    changeStartWeekOption(){
      console.log("changeStartWeekOption");
    },

    getWeekNumber() {
        // Copy date so don't modify original
        var d = new Date(Date.UTC(today.getFullYear(), today.getMonth(), today.getDate()));
        // Set to nearest Thursday: current date + 4 - current day number
        // Make Sunday's day number 7
        d.setUTCDate(d.getUTCDate() + 4 - (d.getUTCDay()||7));
        // Get first day of year
        var yearStart = new Date(Date.UTC(d.getUTCFullYear(),0,1));
        // Calculate full weeks to nearest Thursday
        var weekNo = Math.ceil(( ( (d - yearStart) / 86400000) + 1)/7);
        console.log("mrd weekNo : " + weekNo);
        this.startWeek = weekNo;
        this.endWeek = weekNo;
        this.startWeekYear = today.getFullYear();
        this.endWeekYear = today.getFullYear();
        // Return array of year and week number
    },       

    handleFilter(){
      if(this.optionDate == 1){
        this.query.startDate = commonGetDateFromYearAndWeek(this.startWeek, this.startWeekYear, "from")
        console.log("mrd startDate: " + this.query.startDate)
        this.query.endDate = commonGetDateFromYearAndWeek(this.endWeek, this.endWeekYear, "end")
        console.log("mrd endDate: " + this.query.endDate)

      }else if(this.optionDate == 2){
        this.query.startDate = commonGetDateFromYearAndMonth(this.startMonth, this.startMonthYear,"from")
        this.query.endDate = commonGetDateFromYearAndMonth(this.endMonth, this.endMonthYear, "end")
        console.log("startDate = " + this.query.startDate +   " ,month = " + this.startMonth + ", year = " + this.startMonthYear);
        console.log("EndDate = " + this.query.endDate +   " ,month = " +  this.endMonth + ", endMonthYear : " + this.endMonthYear);
      }
      
      if(this.query.startDate > this.query.endDate){
        this.$notify({
          title: "Có lỗi xảy ra",
          message: "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!",
          type: "error",
        }); 
        return;
      }

      this.query.currentPage = 1;
      this.getList();
      this.status_update = true;
    },
    //build line chartist
    initChartistFlrlToBuild(dataAll, lables){
      let text_type_date = "Tháng/Ngày";
      let datasets = new Array();
      let backgroud = commomColor();
      let i = 0;
      dataAll.forEach(element => {
          var data_chartist = new Array(lables.length);
          let lable = null;
          lables.forEach(function(value,key) {
            let record = element[1].filter(e => e.id == value);
            if(record.length > 0){
              data_chartist[key] = (record[0].byteCount);
              lable = record[0].name;
            }else{
              data_chartist[key] = 0;
            }
          });
          datasets.push({
            label: lable,
            data: data_chartist,
            borderColor: backgroud[i],
          });
          i++;
      });

      this.lineChartData.labels = lables
      this.lineChartData.datasets = datasets;
      this.forceRerender()

    },

   forceRerender() {
       // Remove my-component from the DOM
       this.renderComponent = false;

       this.$nextTick(() => {
         // Add the component back in
         this.renderComponent = true;
       });
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

.title-tree {
    margin-top: 15px;
    padding: 5px 15px;
    background-color: #25465b;
    color: #ffffff;
    font-size: 16px;
    text-transform: uppercase;
    font-weight: 500;
}

//pcap
fieldset {
    display: block;
    margin-left: 2px;
    margin-right: 2px;
    margin-bottom: 15px;
    padding-top: 0.25em;
    padding-bottom: 0.625em;
    padding-left: 0.75em;
    padding-right: 0.75em;
}

table.dataTable tbody td {
    vertical-align: middle;
}
#event_result {
    padding: 8px;
    display: inline-block;
    background: #25465b;
    color: #fff;
    margin: 15px 0px 15px 0px;
}
#mediaChartJsPieDoughnutProtocolTotalNowInitDiv > .card-content > .card-body {
    border: 1px solid #25465b1f;
}
.startTime-endTime {
	font-size: 13px;
}
.title-tree {
	margin-top: 15px;
	padding: 5px 15px;
	background-color: #25465b;
	color: #ffffff;
    font-size: 16px;
    text-transform: uppercase;
    font-weight: 500;
}

.chartist-line {
    padding: 0px;
    border-top: 1px solid #00000024;
}
#chartistFlrlTableList {
	font-size: 13px;
    font-family: Arial;
}
#chartist-pie .card-header {
	border-top: 1px solid #0000001a;
}
#chartist-pie {
    height: 90vh;
    overflow-y: scroll;
}
.bootstrap-select .dropdown-toggle .filter-option-inner-inner {
   font-size: 11px;
}
.custom-checkbox {
	margin: 10px 0;
}
.custom-control-label {
	color: #25465b;
    font-weight: 700;
}
@media screen and (max-device-width: 1366px) {
    .home-height-canvas{
        height: 175px;
    }
}

@import '../../../node_modules/jstree/dist/themes/default/style.css';

</style>
<style lang="scss">
.form-value {
  .el-input__inner {
    margin-bottom: 10px;
  }
}
.el-input--small .el-input__inner {
  height: 36px;
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