<template>
    <div>
        <Dialog :show="dialogConfig.show" :title="dialogConfig.title" :buttons="dialogConfig.buttons" width="400px"
            :showCancel="false" @close="dialogConfig.show = false">
            <el-form :model="formData" ref="formDataRef" label-width="80px" @submit.prevent>
                <!--input输入-->
                <el-form-item label="昵称">
                    {{formData.nickName}}
                </el-form-item>
                <el-form-item label="头像" prop="">
                    <Avatar-upload v-model="formData.avatar"></Avatar-upload>
                </el-form-item>
            </el-form>
        </Dialog>
    </div>
</template>

<script setup>
import AvatarUpload from "@/components/AvatarUpload.vue";
import { ref, reactive, getCurrentInstance, nextTick } from "vue"
import { useRouter, useRoute } from "vue-router"
const { proxy } = getCurrentInstance();

const api = {
    updateUserAvatar: "updateUserAvatar",
};

const formData = ref({});
const formDataRef = ref();

const show = (data) => {
    formData.value = Object.assign({}, data)
    formData.value.avatar = { userId: data.userId, qqAvatar: data.avatar }
    dialogConfig.show = true;
};

defineExpose({ show });

const dialogConfig = reactive({
    show: false,
    title: "修改头像",
    buttons: [
        {
            type: "primary",
            text: "确定",
            click: (e) => {
                submitForm();
            },
        },
    ],
});
const emit = defineEmits();
const submitForm = async () => {
    if (!(formData.value.avatar instanceof File)) {
        dialogConfig.show = false;
        return;
    }

    let result = await proxy.Request({
        url: api.updateUserAvatar,
        params: {
            avatar: formData.value.avatar,
        }
    })
    if (!result) {
        return;
    }
    dialogConfig.show = false;
    const cookieUserInfo = proxy.VueCookies.get("userInfo");
    delete cookieUserInfo.avatar;
    proxy.VueCookies.set("userInfo", cookieUserInfo, 0);
    emit("updateAvatar")
}
</script>

<style lang="scss" scoped></style>
