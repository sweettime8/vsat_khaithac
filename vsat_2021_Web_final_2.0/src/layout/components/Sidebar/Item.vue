<script>
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    imgIcon:{
      type:String,
      default:null
    },
    icon: {
      type: String,
      default: '',
    },
    title: {
      type: String,
      default: '',
    },
  },
  render(h, context) {
    const { icon, title,imgIcon } = context.props;
    const vnodes = [];
    console.log('build menu item: ',context.props)
    if (icon && icon.startsWith('el-')) {
      vnodes.push(<i class={icon} />);
    } 
    else if(imgIcon){
      vnodes.push(<img src={imgIcon} style="" />);
    }
    else {
      vnodes.push(<svg-icon icon-class={icon}/>);
    }

    if (title) {
      vnodes.push(<span slot='title'>{(title)}</span>);
    }
    return vnodes;
  },
};
</script>

<style lang="scss" scoped>
  .el-submenu [class^="el-icon-"] {
    text-align: left;
  }
</style>
