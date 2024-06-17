import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
//引入elment plus
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
//图标
import "@/assets/icon/iconfont.css";
import "@/assets/base.scss";
//引入cookie
import VueCookies from "vue-cookies";

import verify from "@/utils/Verify";

//自定义组件
import Dialog from "@/components/Dialog.vue";
import Avatar from "@/components/Avatar.vue";
import Table from "@/components/Table.vue";
import Icon from "@/components/Icon.vue";
import NoData from "@/components/NoData.vue";

import Verify from "@/utils/Verify";
import Message from "@/utils/Message";
import Request from "@/utils/Request";
import Confirm from "@/utils/Confirm";
import Utils from "@/utils/Utils";

const app = createApp(App);
app.use(router);
app.use(ElementPlus);
// app.use;
app.component("Dialog", Dialog);
app.component("Avatar", Avatar);
app.component("Table", Table);
app.component("Icon", Icon);
app.component("NoData", NoData);
//配置全局组件
app.config.globalProperties.Verify = Verify;
app.config.globalProperties.Message = Message;
app.config.globalProperties.Request = Request;
app.config.globalProperties.VueCookies = VueCookies;
app.config.globalProperties.Confirm = Confirm;
app.config.globalProperties.Utils = Utils;
app.config.globalProperties.globalInfo = {
  avatarUrl: "/api/getAvatar/",
  imageUrl: "/api/file/getImage/",
};
app.mount("#app");
